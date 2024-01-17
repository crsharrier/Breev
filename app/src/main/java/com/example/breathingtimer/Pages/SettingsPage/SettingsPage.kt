package com.example.breathingtimer.Pages.SettingsPage

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.breathingtimer.Home.AppPage
import com.example.breathingtimer.Home.BreevAppState
import com.example.breathingtimer.Home.BreevSetting
import com.example.breathingtimer.Home.SessionPhase
import com.example.breathingtimer.ui.theme.Oxygen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun SettingsPage(
    appState: BreevAppState
) {
    val breevBlob = appState.breevBlob
    val settingsWidget = appState.settingsWidget

    breevBlob.setSize(breevBlob.inner, 700.dp)
    breevBlob.resetOffset()
    settingsWidget.setVerticalSpacer(0.dp)
    settingsWidget.setLineSpacing(50.dp)
}

class SettingsWidget(
    val appState: BreevAppState,
    val navController: NavController
) {
    val viewModel = appState.viewModel

    companion object {
        const val defaultVerticalSpacer = 500
        const val defaultLineSpacing = 5
    }

    val vSpacer = MutableStateFlow(defaultVerticalSpacer.dp)
    private val _lineSpacing = MutableStateFlow(defaultLineSpacing.dp)

    @Composable
    fun DrawSettingsWidget() {
        val totalRounds by viewModel.totalRoundsSetting.currentValue.collectAsState()
        val totalBreaths by viewModel.totalBreathsSetting.currentValue.collectAsState()
        val countdownFrom by viewModel.countdownFromSetting.currentValue.collectAsState()
        val breathSpeed by viewModel.breathSpeedSetting.currentValue.collectAsState()
        val breathSpeedDiscrete =
            when (breathSpeed) {
                in 2000..3999 -> "fast"
                in 4000..5999 -> "med"
                else -> "slow"
            }
        val secondsValue = (breathSpeed / 1000).toString()
        val interactionSource = MutableInteractionSource()
        val currentRoute = appState.currentDestination?.route

        Column(
            Modifier
                .fillMaxWidth(0.9f),
                /*.clickable(
                    interactionSource = interactionSource,
                    indication = null
                ){
                 if (currentRoute == "Timer")
                     navController.navigate("Settings")
                }*/
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start
        ) {
            SettingsRow(
                label = "rounds",
                valueString = totalRounds.toString(),
                setting = viewModel.totalRoundsSetting
            )
            SettingsRow(
                label = "breaths per round",
                valueString = totalBreaths.toString(),
                setting = viewModel.totalBreathsSetting
            )
            SettingsRow(
                label = "countdown",
                valueString = countdownFrom.toString(),
                setting = viewModel.countdownFromSetting
            )
            SettingsRow(
                label = "breath speed",
                valueString = breathSpeedDiscrete,
                setting = viewModel.breathSpeedSetting,
                secondsValue = secondsValue
            )
        }
    }

    fun setVerticalSpacer(newSpacerSize: Dp) {
        vSpacer.value = newSpacerSize
    }

    fun setLineSpacing(newSpacing: Dp) {
        _lineSpacing.value = newSpacing
    }

    @Composable
    fun SettingsRow(
        label: String,
        valueString: String,
        setting: BreevSetting,
        secondsValue: String? = null
    ) {
        val currentValue by setting.currentValue.collectAsState()
        val lineSpacing by _lineSpacing.collectAsState()
        val lineSpacingAnimation by animateDpAsState(
            targetValue = lineSpacing,
            animationSpec = tween(500)
        )

        Column (
            horizontalAlignment = Alignment.End
                ){
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    //------------------------------------------------
                    Row(
                        Modifier
                            //.border(1.dp, Color.Magenta)
                            .fillMaxWidth(0.55f),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "$label  :  ",
                            fontFamily = Oxygen,
                            fontWeight = FontWeight.Light,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Right
                        )
                    }
                    //------------------------------------------------
                    Row(
                        Modifier
                            //.border(1.dp, Color.Cyan)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.width(80.dp),
                            text = valueString,
                            fontFamily = Oxygen,
                            fontWeight = FontWeight.Normal,
                            fontSize = 30.sp,
                        )
                        if (appState.currentDestination?.route == AppPage.Settings.route){
                            if (secondsValue != null){
                                Text(text = "($secondsValue secs)", fontSize = 15.sp)
                            }
                        }
                    }
                    //------------------------------------------------
                }
                val sliderIsVisible = appState.currentDestination?.route == AppPage.Settings.route
                if (sliderIsVisible) {
                    var sliderAlpha by remember { mutableFloatStateOf(0f) }
                    val sliderAlphaAnimation by animateFloatAsState(
                        targetValue = sliderAlpha,
                        animationSpec = tween(500)
                    )
                    LaunchedEffect(sliderIsVisible) {
                        delay(250)
                        sliderAlpha = 1f
                    }

                    SettingSlider(
                        modifier = Modifier.alpha(sliderAlphaAnimation),
                        value = currentValue,
                        valueRange = setting.range,
                        steps = setting.steps,
                        onValueChange = setting.setter
                    )
                }
            }
            Spacer(Modifier.size(lineSpacingAnimation))
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SettingSlider(
        modifier: Modifier,
        value: Int,
        valueRange: ClosedFloatingPointRange<Float>,
        steps: Int,
        onValueChange: (Int) -> Unit
    ){
        Slider(
            modifier = modifier
                .fillMaxWidth(0.7f),
            value = value.toFloat(),
            valueRange = valueRange,
            colors = SliderDefaults.colors(
                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            steps = steps,
            onValueChange = {onValueChange(it.toInt())}
        )
    }
}