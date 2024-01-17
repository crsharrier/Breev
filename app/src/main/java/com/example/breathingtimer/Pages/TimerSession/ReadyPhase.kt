package com.example.breathingtimer.Pages.TimerSession

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.breathingtimer.Home.BreevAppState
import com.example.breathingtimer.Home.BreevBlob
import com.example.breathingtimer.Home.SessionPhase
import com.example.breathingtimer.Home.TimerSessionViewModel
import com.example.breathingtimer.ui.theme.Oxygen
import kotlinx.coroutines.delay

@Composable
fun ReadyPhase(
    appState: BreevAppState
) {
    Log.d("ReadyPhase", "ReadyPhase started")
    val viewModel = appState.viewModel
    val breevBlob = appState.breevBlob

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Countdown(appState)
        Spacer(Modifier.size(80.dp))
        RoundsRemainingText(viewModel)
        Spacer(Modifier.size(20.dp))
        GetReadyText()
    }
}

@Composable
fun Countdown(
    appState: BreevAppState
) {
    val viewModel = appState.viewModel
    val breevBlob = appState.breevBlob

    val countdownFrom by viewModel.countdownFromSetting.currentValue.collectAsState()
    var count by remember { mutableIntStateOf(countdownFrom) }

    Text(
        text = if (count == 0) "1" else "$count",
        fontFamily = Oxygen,
        fontWeight = FontWeight.Bold,
        fontSize = 70.sp,
    )

    //Decrement countdown
    LaunchedEffect(key1 = count) {
        Log.d("ReadyPhase", "Countdown Launched Effect")
        if (count > 0) {
            if (count < 2) {
                breevBlob.setSize(
                    breevBlob.inner,
                    BreevBlob.innerBreathePhaseMin.dp,
                    BreevBlob.innerBreathePhaseMax.dp
                )
                breevBlob.setSize(
                    breevBlob.outer,
                    BreevBlob.outerBreathePhaseMin.dp,
                    BreevBlob.outerBreathePhaseMax.dp
                )
            }
            delay(1000L)
            count -= 1
        } else {
            viewModel.resetBreathCount()
            viewModel.setPhase(SessionPhase.BREATHE)
        }
    }
}

@Composable
fun RoundsRemainingText(
    viewModel: TimerSessionViewModel
) {
    val totalRounds by viewModel.totalRoundsSetting.currentValue.collectAsState()
    val currentRound by viewModel.currentRound.collectAsState()

    Row {
        if (totalRounds - currentRound + 1 > 1) {
            Text(
                text = (totalRounds - currentRound + 1).toString(),
                fontFamily = Oxygen,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
            )
            Text(
                text = " rounds remaining",
                fontFamily = Oxygen,
                fontWeight = FontWeight.Normal,
                fontSize = 25.sp,
            )
        } else {
            Text(
                text = "Final round!",
                fontFamily = Oxygen,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
            )
        }
    }
}

@Composable
fun GetReadyText() {
    Row {
        Text(
            text = " Get ready to ",
            fontFamily = Oxygen,
            fontWeight = FontWeight.Light,
            fontSize = 20.sp,
        )
        Text(
            text = "breathe in",
            fontFamily = Oxygen,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
        )
    }
}