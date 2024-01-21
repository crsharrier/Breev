package com.example.breev.pages.TimerSession

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.breev.home.BreevAppState
import com.example.breev.home.SimpleCircleShape
import com.example.breev.home.TimerSessionViewModel
import com.example.breev.pages.SettingsPage.SettingsWidget
import com.example.breev.ui.theme.Oxygen

@Composable
fun NotStartedPhase(
    appState: BreevAppState
) {
    val breevBlob = appState.breevBlob
    val settingsWidget = appState.settingsWidget

    breevBlob.ResetBlobs()
    settingsWidget.setVerticalSpacer(
        SettingsWidget.defaultVerticalSpacer.dp
    )
    settingsWidget.setLineSpacing(
        SettingsWidget.defaultLineSpacing.dp
    )
    StartButton(appState.viewModel)
}

@Composable
fun StartButton(
    viewModel: TimerSessionViewModel
) {
    val interactionSource = MutableInteractionSource()
    Box(
        contentAlignment = Alignment.Center,
    ) {
        SimpleCircleShape(
            modifier = Modifier.clickable(
                interactionSource = interactionSource,
                indication = null
            ) { viewModel.clickStartButton() },
            size = 250.dp,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            modifier = Modifier.width(250.dp),
            text = "start session",
            fontFamily = Oxygen,
            fontWeight = FontWeight.Normal,
            fontSize = 40.sp,
            lineHeight = 50.sp,
            textAlign = TextAlign.Center,
            color = Color.White
        )
    }
}