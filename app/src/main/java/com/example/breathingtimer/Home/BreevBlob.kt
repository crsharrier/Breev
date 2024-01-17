package com.example.breathingtimer.Home

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.breathingtimer.Pages.TimerSession.StartButton
import com.example.breathingtimer.ui.theme.BreathingTimerTheme
import kotlinx.coroutines.flow.MutableStateFlow



data class Blob(
    val min: MutableStateFlow<Dp>,
    val max: MutableStateFlow<Dp>,
    val color: MutableStateFlow<Color>,
    val yOffset: MutableStateFlow<Float>
)

//The morphing shape which frames the content on every screen,
// and 'breathes' during the breathing sessions
class BreevBlob(
    val viewModel: TimerSessionViewModel
) {
    companion object {
        const val defaultInnerSize = 400f
        const val defaultOuterSize = 450f
        const val innerBreathePhaseMin = 120f
        const val innerBreathePhaseMax = 450f
        const val outerBreathePhaseMin = 200f
        const val outerBreathePhaseMax = 500f
    }

    val inner = Blob(
        min = MutableStateFlow(0.dp),
        max = MutableStateFlow(0.dp),
        color = MutableStateFlow(Color.Transparent),
        yOffset = MutableStateFlow(0f)
    )

    val outer = Blob(
        min = MutableStateFlow(0.dp),
        max = MutableStateFlow(0.dp),
        color = MutableStateFlow(Color.Transparent),
        yOffset = MutableStateFlow(0f)
    )

    private val blobAnimationCompleted: (Dp) -> Unit = {
        Log.d("BreevBlob", "blobAnimationCompleted")
        viewModel.blobAnimationCompleted()
    }

    @Composable
    fun DrawBlob(blob: Blob) {
        val currentPhase by viewModel.currentPhase.collectAsState()
        val breathSpeed by viewModel.breathSpeedSetting.currentValue.collectAsState()
        val minSize by blob.min.collectAsState()
        val maxSize by blob.max.collectAsState()
        val shouldBreatheIn by viewModel.shouldBreatheIn.collectAsState()
        val animationDurationMillis =
            if (currentPhase == SessionPhase.BREATHE)
                breathSpeed / 2
            else
                950
        val blobSizeAnimation by animateDpAsState(
            targetValue = if (currentPhase == SessionPhase.BREATHE && shouldBreatheIn)
                maxSize else minSize,
            animationSpec = tween(animationDurationMillis),
            finishedListener = blobAnimationCompleted
        )

        val blobYOffset by blob.yOffset.collectAsState()
        val blobYOffsetAnimation by animateFloatAsState(
            targetValue = blobYOffset,
            animationSpec = tween(animationDurationMillis)
        )

        val blobColor by blob.color.collectAsState()
        val animatedColor by animateColorAsState(blobColor)

        Box(
            Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.7f)
                .offset(y = blobYOffsetAnimation.dp),
            contentAlignment = Alignment.Center
        ) {
            SimpleCircleShape(
                size = blobSizeAnimation,
                color = animatedColor
            )
        }
    }

    @Composable
    fun ResetBlobs() {
        setColor(inner, MaterialTheme.colorScheme.primaryContainer.copy(0.8f))
        setColor(outer, MaterialTheme.colorScheme.primaryContainer.copy(0.3f))
        setSize(inner, defaultInnerSize.dp)
        setSize(outer, defaultOuterSize.dp)
        resetOffset()
    }

    fun setColor(blob: Blob, newColor: Color){
        blob.color.value = newColor
    }

    fun setSize(blob: Blob, min: Dp, max: Dp){
        blob.min.value = min
        blob.max.value = max
        Log.d("breevBlob.setSize", "newMin = $min, newMax = $max")
    }

    fun setSize(blob: Blob, size: Dp){
        blob.min.value = size
        blob.max.value = size
        Log.d("breevBlob.setSize", "newMin = $size, newMax = $size")
    }

    fun setYOffset(blob: Blob, yOffset: Float) {
        blob.yOffset.value = yOffset
        Log.d("breevBlob.setYOffset", "newOffset = $yOffset")
    }

    fun resetOffset(){
        inner.yOffset.value = 0f
        outer.yOffset.value = 0f
    }
}

@Composable
fun SimpleCircleShape(
    modifier: Modifier = Modifier,
    size: Dp,
    color: Color = MaterialTheme.colorScheme.primary,
    borderWidth: Dp = 0.dp,
    borderColor: Color = Color.LightGray.copy(alpha = 0.0f)
) {
    Box(
        modifier = modifier
            //.animateContentSize()
            .size(size)
            .clip(CircleShape)
            .background(color)
            .border(borderWidth, borderColor)
    )
}

