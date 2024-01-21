package com.example.breev.home

import android.os.SystemClock
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.breev.datastore.BreevSetting
import com.example.breev.datastore.BreevSettings
import com.example.breev.datastore.SessionDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class TimerSessionViewModel(
    val breevSettingsStore: DataStore<BreevSettings>,
    //private val sessionDataRepository: DataStore<BreevSettingsAndData>
) : ViewModel() {

    // --------------------------------------------------
    //Datastore:
    // --------------------------------------------------

    val sessionDatabase = SessionDatabase()

    // --------------------------------------------------
    //Settings:
    // --------------------------------------------------

    companion object {
        const val totalRoundsMin = 1
        const val totalRoundsMax = 5
        const val totalBreathsMin = 10
        const val totalBreathsMax = 40
        const val countdownMin = 1
        const val countdownMax = 10
        const val breathSpeedQuickest = 2000
        const val breathSpeedSlowest = 7000
    }
/*
    private fun setTotalRounds(totalRounds: Int) {
        viewModelScope.launch {
            breevSettingsRepository.setTotalRounds(totalRounds)
        }
    }
 */
    private suspend fun setTotalRounds(totalRounds: Int) {
        breevSettingsStore.updateData {
            it.copy(
                totalRounds = totalRounds
            )
        }
    }

    private suspend fun setTotalBreaths(totalBreaths: Int) {
        breevSettingsStore.updateData {
            it.copy(
                totalBreaths = totalBreaths
            )
        }
    }

    private suspend fun setCountdownFrom(countdownFrom: Int) {
        breevSettingsStore.updateData {
            it.copy(
                countdownFrom = countdownFrom
            )
        }
    }

    private suspend fun setBreathSpeed(breathSpeed: Int) {
        breevSettingsStore.updateData {
            it.copy(
                breathSpeed = breathSpeed
            )
        }
    }

    private val _settingsState = MutableStateFlow(BreevSettings())
    val settingsState: StateFlow<BreevSettings> = _settingsState

    //private val _totalRounds = MutableStateFlow(2)
    val totalRoundsSetting = BreevSetting(
        name = "rounds",
        max = totalRoundsMax,
        min = totalRoundsMin,
        setter = { viewModelScope.launch{ setTotalRounds(it) } }
    )

    val totalBreathsSetting = BreevSetting(
        name = "breaths",
        max = totalBreathsMax,
        min = totalBreathsMin,
        setter = { viewModelScope.launch{ setTotalBreaths(it) } }
    )

    val countdownFromSetting = BreevSetting(
        name = "countdown",
        max = countdownMax,
        min = countdownMin,
        setter = { viewModelScope.launch{setCountdownFrom(it) } }
    )

    val breathSpeedSetting = BreevSetting(
        name = "speed",
        max = breathSpeedSlowest,
        min = breathSpeedQuickest,
        steps = 0,
        setter = { viewModelScope.launch{ setBreathSpeed(it) } }
    )

    // --------------------------------------------------
    // History:
    // --------------------------------------------------

    enum class DataRange(
        val asInt: Int
    ){
        SEVEN(7),
        THIRTY(30),
        NINETY(90),
        ALL_TIME(0)
    }

    private val _graphDataRange = MutableStateFlow(DataRange.SEVEN)
    val graphDataRange = _graphDataRange.asStateFlow()

    fun setDataRange(newRange: DataRange){
        _graphDataRange.value = newRange
    }

    // --------------------------------------------------
    //FOR DEBUGGING:

    /*
    fun generateSessionData() {
        Log.d("generateSessionData", "session data generated")
        val random = Random.Default
        val now = System.currentTimeMillis()
        val oneDayInSeconds: Long = 1000 * 60 * 60 * 24
        for (i in 1..10){
            val session = BreathingSession()
            session.completeRound(random.nextLong(30000, 60000))
            session.completeRound(random.nextLong(30000, 60000))
            //session.timeStampMillis = now + i
            session.timeStampMillis = now + (i * oneDayInSeconds)
            _sessionDatabase.add(session)
        }
    }
    */


    // --------------------------------------------------
    //Breathing session:
    // --------------------------------------------------
    private var _currentSession = BreathingSession()
    val currentSession: BreathingSession
        get() = _currentSession

    private val _currentPhase = MutableStateFlow(SessionPhase.NOT_STARTED)
    val currentPhase = _currentPhase.asStateFlow()
    fun setPhase(newPhase: SessionPhase) {
        _currentPhase.value = newPhase
    }

    private val _currentRound = MutableStateFlow(1)
    val currentRound = _currentRound.asStateFlow()

    private val _showExitDialog = MutableStateFlow(false)
    val showExitDialog = _showExitDialog.asStateFlow()
    fun toggleExitDialog() {
        _showExitDialog.value = !_showExitDialog.value
    }

    fun exitSession() {
        if (_currentPhase.value == SessionPhase.RESULTS) {
            saveAndClearSession()
            setPhase(SessionPhase.NOT_STARTED)
            _shouldBreatheIn.value = true
        } else if (_currentSession.rounds.isNotEmpty())
            setPhase(SessionPhase.RESULTS)
        else{
            setPhase(SessionPhase.NOT_STARTED)
            _shouldBreatheIn.value = true
        }
    }

    private fun saveAndClearSession() {

    }

    // --------------------------------------------------
    //NOT_STARTED phase:
    // --------------------------------------------------

    fun clickStartButton() {
        setPhase(SessionPhase.READY)
        _currentRound.value = 1
    }

    private val _totalBreaths = _settingsState.value.totalBreaths
    private val _totalRounds = _settingsState.value.totalRounds

    // --------------------------------------------------
    //READY phase:
    // --------------------------------------------------


    fun resetBreathCount() {
        _remainingBreaths.value = _totalBreaths
        _shouldBreatheIn.value = true
        Log.d("resetBreathCount()",
           "resetBreathCount called: remainingBreaths = ${remainingBreaths.value}, shouldBreatheIn = ${_shouldBreatheIn.value}")
    }


    // --------------------------------------------------
    //BREATHE phase:
    // --------------------------------------------------

    private val _remainingBreaths = MutableStateFlow(_totalBreaths)
    val remainingBreaths = _remainingBreaths.asStateFlow()

    private val _shouldBreatheIn = MutableStateFlow(true)
    val shouldBreatheIn = _shouldBreatheIn.asStateFlow()

    private var _oneBlobTriggered = false

    fun blobAnimationCompleted() {
        //Check we are in BREATHE phase
        Log.d("blobAnimationCompleted", "_oneBlobTriggered = $_oneBlobTriggered, currentPhase = ${currentPhase.value}")
        if (_currentPhase.value == SessionPhase.BREATHE) {
            if (_oneBlobTriggered){
                halfBreathTaken()
                _oneBlobTriggered = false
            }
            else
                _oneBlobTriggered = true
        }
    }

    //TODO: Very confusing, ugly conditional
    private fun halfBreathTaken() {
        _shouldBreatheIn.value = !_shouldBreatheIn.value
        Log.d("halfBreathTaken", "breaths remaining = ${_remainingBreaths.value}, shouldBreatheIn = ${_shouldBreatheIn.value}")
        if (_remainingBreaths.value > 0 && _shouldBreatheIn.value) {
            _remainingBreaths.value -= 1
        } else if (_shouldBreatheIn.value)
            startHold()
    }


    // --------------------------------------------------
    //HOLD phase:
    // --------------------------------------------------

    private var _startTime = 0L

    private fun startHold() {
        setPhase(SessionPhase.HOLD)
        val currentTime = SystemClock.uptimeMillis()
        _startTime = currentTime
    }

    fun finishHold() {
        val currentTime = SystemClock.uptimeMillis()
        val timeElapsed = currentTime - _startTime
        _currentSession.completeRound(timeElapsed)
        _shouldBreatheIn.value = true

        //if there are no more rounds to be completed:
        if (_currentRound.value >= _totalRounds) {
            setPhase(SessionPhase.RESULTS)
        } else {
            setPhase(SessionPhase.READY)
            _currentRound.value += 1
        }
    }
}