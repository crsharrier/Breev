package com.example.breathingtimer.Home

import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

data class BreevSetting(
    val name: String,
    val currentValue: StateFlow<Int>,
    val max: Int,
    val min: Int,
    val range: ClosedFloatingPointRange<Float> = min.toFloat()..max.toFloat(),
    val steps: Int = max - min,
    val setter: (Int) -> Unit
)

class TimerSessionViewModel : ViewModel() {

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

    private val _totalRounds = MutableStateFlow(2)
    val totalRoundsSetting = BreevSetting(
        name = "rounds",
        currentValue = _totalRounds.asStateFlow(),
        max = totalRoundsMax,
        min = totalRoundsMin,
        setter = { _totalRounds.value = it }
    )

    private val _totalBreaths = MutableStateFlow(30)
    val totalBreathsSetting = BreevSetting(
        name = "breaths",
        currentValue = _totalBreaths.asStateFlow(),
        max = totalBreathsMax,
        min = totalBreathsMin,
        setter = { _totalBreaths.value = it }
    )

    private val _countdownFrom = MutableStateFlow(5)
    val countdownFromSetting = BreevSetting(
        name = "countdown",
        currentValue = _countdownFrom.asStateFlow(),
        max = countdownMax,
        min = countdownMin,
        setter = { _countdownFrom.value = it }
    )

    private val _breathSpeed = MutableStateFlow(4000)
    val breathSpeedSetting = BreevSetting(
        name = "speed",
        currentValue = _breathSpeed.asStateFlow(),
        max = breathSpeedSlowest,
        min = breathSpeedQuickest,
        steps = 0,
        setter = { _breathSpeed.value = it }
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
    // --------------------------------------------------

    private val _sessionDatabase: MutableList<BreathingSession> = mutableListOf()
    val sessionDatabase: MutableList<BreathingSession>
        get() = _sessionDatabase

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
        _currentSession.timeStampMillis = System.currentTimeMillis()
        _sessionDatabase.add(_currentSession)
        _currentSession = BreathingSession()
    }


    // --------------------------------------------------
    //NOT_STARTED phase:
    // --------------------------------------------------

    fun clickStartButton() {
        setPhase(SessionPhase.READY)
        _currentRound.value = 1
        _currentSession = BreathingSession()
    }


    // --------------------------------------------------
    //READY phase:
    // --------------------------------------------------

    fun resetBreathCount() {
        _remainingBreaths.value = _totalBreaths.value
        _shouldBreatheIn.value = true
        Log.d("resetBreathCount()",
           "resetBreathCount called: remainingBreaths = ${remainingBreaths.value}, shouldBreatheIn = ${_shouldBreatheIn.value}")
    }


    // --------------------------------------------------
    //BREATHE phase:
    // --------------------------------------------------

    private val _remainingBreaths = MutableStateFlow(_totalBreaths.value)
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
        if (_currentRound.value >= _totalRounds.value) {
            setPhase(SessionPhase.RESULTS)
        } else {
            setPhase(SessionPhase.READY)
            _currentRound.value += 1
        }
    }
}