package com.example.breev.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.serialization.Serializable
import java.io.IOException

// SessionDatabase = Map<timestamp, [holdTime, holdTime, holdTime]>

@Serializable
data class SessionDatabase(
    val sessionMap: Map<Long, PersistentList<Long>> = mapOf()
)

class SessionDataRepository(
    private val sessionDatabaseStore: DataStore<SessionDatabase>
) {
    private val TAG: String = "SessionDatabaseRepo"

    val sessionDatabaseFlow: Flow<SessionDatabase> = sessionDatabaseStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                Log.e(TAG, "Error reading sort order preferences.", exception)
                emit(SessionDatabase())
            } else {
                throw exception
            }
        }
}