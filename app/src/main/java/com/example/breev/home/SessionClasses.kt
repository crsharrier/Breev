package com.example.breev.home

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

enum class SessionPhase {
    NOT_STARTED, READY, BREATHE, HOLD, RESULTS
}

data class Round(
    val id: Int,
    val holdTimeMilliseconds: Long
)

class BreathingSession {
    var roundsCompleted: Int = 0
    var averageHoldTimeMillis: Float = 0f
    val averageHoldTimeSecs: Int
        get() = (averageHoldTimeMillis / 1000).toInt()
    val rounds: MutableList<Round> = mutableListOf()
    @PrimaryKey @ColumnInfo(name = "timestamp")
    var timeStampMillis: Long = 0L


    fun completeRound(milliseconds: Long) {
        val id = rounds.size + 1
        val round = Round(id, milliseconds)

        rounds.add(round)
        timeStampMillis = System.currentTimeMillis()
        averageHoldTimeMillis = calculateAverageHoldTime()
        roundsCompleted++
    }

    private fun calculateAverageHoldTime(): Float {
        if (rounds.isEmpty()) {
            return 0f
        }
        val totalMilliseconds = rounds.sumOf { it.holdTimeMilliseconds }
        return totalMilliseconds.toFloat() / rounds.size
    }

    override fun toString(): String {
        return "BreathingSession(roundsCompleted=$roundsCompleted, averageHoldTime=$averageHoldTimeMillis, rounds=$rounds)"
    }
}