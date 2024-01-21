package com.example.breev.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.serialization.Serializable
import java.io.IOException

data class BreevSetting(
    val name: String,
    val max: Int,
    val min: Int,
    val range: ClosedFloatingPointRange<Float> = min.toFloat()..max.toFloat(),
    val steps: Int = max - min,
    val setter: (Int) -> Unit
)

@Serializable
data class BreevSettings(
    val totalRounds: Int = 3,
    val totalBreaths: Int = 30,
    val countdownFrom: Int = 5,
    val breathSpeed: Int = 4000
)

class BreevSettingsRepository(
    private val breevSettingsStore: DataStore<BreevSettings>
) {
    private val TAG: String = "BreevSettingsRepo"

    val breevSettingsFlow: Flow<BreevSettings> = breevSettingsStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                Log.e(TAG, "Error reading sort order preferences.", exception)
                emit(BreevSettings())
            } else {
                throw exception
            }
        }


}






@Serializable
data class BreevSettingsAndData(
    val settings: BreevSettings = BreevSettings(),
    val data: SessionDatabase = SessionDatabase()
)

