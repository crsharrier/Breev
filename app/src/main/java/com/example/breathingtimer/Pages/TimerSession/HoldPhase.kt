package com.example.breathingtimer.Pages.TimerSession

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.breathingtimer.Home.BreevAppState
import com.example.breathingtimer.ui.theme.Oxygen

@Composable
fun HoldPhase(
    appState: BreevAppState
){
    val interactionSource = MutableInteractionSource()
    val viewModel = appState.viewModel
    val breevBlob = appState.breevBlob

    breevBlob.setSize(breevBlob.inner, 0.dp)
    breevBlob.setSize(breevBlob.outer, 200.dp, 500.dp)
    Box(
        Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ){ viewModel.finishHold() },
        contentAlignment = Alignment.Center
    ){
        Text(
            text = " Hold ",
            fontFamily = Oxygen,
            fontWeight = FontWeight.Normal,
            fontSize = 40.sp,
        )
        Column (
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(Modifier.size(200.dp))
            Text(
                modifier = Modifier.fillMaxWidth(0.6f),
                text = " Tap screen when you have reached your limit ",
                fontFamily = Oxygen,
                fontWeight = FontWeight.Light,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}