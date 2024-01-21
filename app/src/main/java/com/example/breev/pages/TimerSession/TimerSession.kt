package com.example.breev.pages.TimerSession

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.breev.home.BreevAppState
import com.example.breev.home.SessionPhase

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
