package com.example.breathingtimer.Pages.TimerSession

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.breathingtimer.Home.BreevAppState
import com.example.breathingtimer.Home.SessionPhase

@Composable
fun TimerSession(
    appState: BreevAppState
) {
    val viewModel = appState.viewModel
    val currentPhase by viewModel.currentPhase.collectAsState()
    val showExitDialog by viewModel.showExitDialog.collectAsState()
    val shouldBreatheIn by viewModel.shouldBreatheIn.collectAsState()

    // --------------------------------------------------
    //FOR DEBUGGING:
    /*Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopStart
    ){
        Text("shouldBreatheIn = $shouldBreatheIn")
    }*/
    // --------------------------------------------------

    Box (
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
            ){
        when (currentPhase) {
            SessionPhase.NOT_STARTED ->
                NotStartedPhase(appState)

            SessionPhase.READY ->
                ReadyPhase(appState)

            SessionPhase.BREATHE ->
                BreathePhase(appState)

            SessionPhase.HOLD ->
                HoldPhase(appState)

            SessionPhase.RESULTS ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ResultsPhase(viewModel)
                }
        }
    }
    if (currentPhase != SessionPhase.NOT_STARTED)
        ExitButton(appState)
    if (showExitDialog)
        ExitDialog(appState)

}
