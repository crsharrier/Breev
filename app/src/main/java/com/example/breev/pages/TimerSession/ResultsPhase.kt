package com.example.breev.pages.TimerSession

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.breev.home.TimerSessionViewModel
import com.example.breev.ui.theme.Oxygen


@Composable
fun ResultsPhase(
    viewModel: TimerSessionViewModel
) {
    Column(
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Well Done!",
            fontFamily = Oxygen,
            fontWeight = FontWeight.Normal,
            fontSize = 25.sp,
        )
        Spacer(Modifier.size(50.dp))
        Text(
            modifier = Modifier.fillMaxWidth(0.8f),
            text = "Results for the current session:",
            textAlign = TextAlign.Left,
            fontFamily = Oxygen,
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp,
        )
        Box(
            Modifier.fillMaxWidth(0.8f).height(2.dp).background(Color.LightGray)
        )
        Spacer(Modifier.size(20.dp))
        TimingsInfo(viewModel)
    }
}

@Composable
fun TimingsInfo(
    viewModel: TimerSessionViewModel
) {
    Column(
        Modifier.fillMaxWidth(0.8f)
    ) {
        val currentSession = viewModel.currentSession
        currentSession.rounds.forEach { round ->
            val id = round.id
            val seconds = round.holdTimeMilliseconds / 1000
            val secondOrSeconds = if (seconds > 1) "seconds" else "second"
            Row{
                Text(
                    text = "Round $id \t: ",
                    fontFamily = Oxygen,
                    fontWeight = FontWeight.Light,
                    fontSize = 18.sp,
                )
                Text(
                    text = "$seconds $secondOrSeconds",
                    fontFamily = Oxygen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )
            }
        }
        val averageSeconds = currentSession.averageHoldTimeMillis / 1000
        val secondOrSeconds = if (averageSeconds > 1) "seconds" else "second"
        Spacer(Modifier.size(50.dp))
        Text(
            text = "Average hold time:",
            fontFamily = Oxygen,
            fontWeight = FontWeight.Normal,
            fontSize = 22.sp
        )
        Text(
            text = "${averageSeconds.toString().format("%.2f")} $secondOrSeconds",
            fontFamily = Oxygen,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
        )
    }
}