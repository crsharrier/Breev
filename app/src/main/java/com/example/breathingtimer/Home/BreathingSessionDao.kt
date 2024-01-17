package com.example.breathingtimer.Home

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert

@Dao
interface BreathingSessionDao {
    @Insert
    suspend fun insertSession(session: BreathingSession)

    @Delete
    suspend fun deleteSession(session: BreathingSession)
}