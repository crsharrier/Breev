package com.example.breathingtimer.Pages.TimerSession

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.breathingtimer.Home.BreevAppState
import com.example.breathingtimer.Home.TimerSessionViewModel
import com.example.breathingtimer.ui.theme.Oxygen

@Composable
fun BreathePhase(
    appState: BreevAppState
) {
    Log.d("BreathePhase", "BreathePhase started")
    val viewModel = appState.viewModel

    Box(
        contentAlignment = Alignment.Center
    ) {
        BreatheText(viewModel)
        BreathCounterText(viewModel)
    }
}

@Composable
fun BreatheText(
    viewModel: TimerSessionViewModel
) {
    val shouldBreatheIn by viewModel.shouldBreatheIn.collectAsState()
    //Log.d("TEXT", "shouldBreatheIn = $shouldBreatheIn")

    Text(
        text = if (shouldBreatheIn) "Breathe in" else "Breathe out",
        style = TextStyle().copy(color = MaterialTheme.colorScheme.primary),
        fontSize = 30.sp,
    )
}

@Composable
fun BreathCounterText(
    viewModel: TimerSessionViewModel
) {
    val shouldBreatheIn by viewModel.shouldBreatheIn.collectAsState()
    //Log.d("BreathePhase", "breath counter: shouldBreathein = $shouldBreatheIn")

    val remainingBreaths by viewModel.remainingBreaths.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.size(300.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.width(80.dp),
                //.border(BorderStroke(1.dp, Color.Black)),
                text = remainingBreaths.toString(),
                fontFamily = Oxygen,
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                textAlign = TextAlign.Right
            )
            Text(
                //modifier= Modifier.border(BorderStroke(1.dp, Color.Blue)),
                text = " breaths left",
                fontFamily = Oxygen,
                fontWeight = FontWeight.Light,
                fontSize = 30.sp,
                //textAlign = TextAlign.Center
            )
        }
    }
}