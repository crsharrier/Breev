package com.example.breev.pages.TimerSession

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.breev.home.BreevAppState
import com.example.breev.ui.theme.Oxygen

@Composable
fun ExitButton(
    appState: BreevAppState
) {
    val viewModel = appState.viewModel

    val interactionSource = MutableInteractionSource()
    Box(
        Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopStart
    ) {
        Icon(
            modifier = Modifier
                .size(70.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    viewModel.toggleExitDialog()
                },
            imageVector = Icons.Filled.Close,
            tint = Color.LightGray,
            contentDescription = null
        )
    }
}

@Composable
fun ExitDialog(
    appState: BreevAppState
){
    val viewModel = appState.viewModel
    val breevBlob = appState.breevBlob

    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        var currentState = remember { MutableTransitionState(false) }
        currentState.targetState = true
        val transition = updateTransition(currentState, label = "exit dialog box")
        val dialogSize by transition.animateDp(
            label = "exit dialog size"
        ) { state ->
            when (state) {
                false -> 1.dp
                true -> 350.dp
            }
        }

        val dialogWindowGradient =
            Brush.radialGradient(
                    0.7f to MaterialTheme.colorScheme.primaryContainer,
                    0.8f to MaterialTheme.colorScheme.primaryContainer.copy(0.8f),
                    0.9f to MaterialTheme.colorScheme.primaryContainer.copy(0.2f)
            )
        Column(
            Modifier
                //TODO: MAGIC NUMBER
                .size(dialogSize)
                .background(
                    dialogWindowGradient,
                    CircleShape
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Text (
                "Quit this session?",
                fontSize = 30.sp,
                lineHeight = 50.sp,
                textAlign = TextAlign.Center,
                fontFamily = Oxygen
            )
            Spacer(Modifier.size(10.dp))
            Row(
                Modifier.padding(15.dp)
            ){
                // 'NO' Button
                Button(
                    modifier = Modifier.wrapContentSize(),
                    onClick = {
                        viewModel.toggleExitDialog()
                    }
                ){
                    Icon(
                        modifier = Modifier.size(60.dp),
                        imageVector = Icons.Filled.Close,
                        tint = Color.LightGray,
                        contentDescription = null
                    )
                }
                Spacer(Modifier.size(20.dp))

                // 'YES' button
                Button(
                    modifier = Modifier.wrapContentSize(),
                    onClick = {
                        viewModel.exitSession()
                        viewModel.toggleExitDialog()
                    }
                ){
                    Icon(
                        modifier = Modifier.size(60.dp),
                        imageVector = Icons.Filled.Done,
                        tint = Color.LightGray,
                        contentDescription = null
                    )
                }
            }
        }
    }
}
