package com.example.breathingtimer.Pages.HistoryPage

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeCompilerApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.yml.charts.common.model.Point
import com.example.breathingtimer.Home.BreevAppState
import com.example.breathingtimer.Home.TimerSessionViewModel
import kotlinx.coroutines.delay

@Composable
fun HistoryPage(
    appState: BreevAppState
) {
    val breevBlob = appState.breevBlob
    val viewModel = appState.viewModel
    val graph = appState.graph


    val graphYOffset = -150f
    val infoYOffset = 200f

    var graphAlpha by remember { mutableStateOf(0f) }

    breevBlob.setSize(breevBlob.inner, 600.dp)
    breevBlob.setSize(breevBlob.outer, 500.dp)
    breevBlob.setYOffset(breevBlob.inner, graphYOffset)
    breevBlob.setYOffset(breevBlob.outer, infoYOffset)

    val graphFadeAnimation by animateFloatAsState(
        targetValue = graphAlpha,
        animationSpec = tween(500)
    )

    LaunchedEffect(graphYOffset) {
        delay(800)
        graphAlpha = 1f
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .offset(y = graphYOffset.dp)
            .alpha(graphFadeAnimation),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        graph.BreevLinechart()
    }
    Column(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .offset(y = infoYOffset.dp)
            .alpha(graphFadeAnimation),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (viewModel.sessionDatabase.isEmpty()){
            NoDataWarning()
        }
        else {
            //val highestAverageHoldTime = (graph.pointsData!!.maxBy { it.y }).y
            DataRangeSelector(viewModel)
            //Text("Personal Best: $highestAverageHoldTime")
            //PointsDataHead(testPointsData)
            //graph.PointsDataHead()
            //Text("Axes  ranges = ${graph.getAxesRanges(graph.pointsData)}")
        }

    }
}

@Composable
fun NoDataWarning(){
    Text(
        modifier = Modifier
            .fillMaxWidth(0.8f),
        text = "Check back here after you have completed a breathing session")
}

@Composable
fun DataRangeSelector(
    viewModel: TimerSessionViewModel
) {
    val currentDataRange by viewModel.graphDataRange.collectAsState()
    Column (
        modifier = Modifier.fillMaxWidth(0.8f),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.End
            ){
        for (range in TimerSessionViewModel.DataRange.values()) {
            DataRangeSelectorButton(
                dataRange = range,
                selected = range == currentDataRange
            ) { viewModel.setDataRange(range) }
        }
    }
}

@Composable
fun DataRangeSelectorButton(
    dataRange: TimerSessionViewModel.DataRange,
    selected: Boolean,
    callback: () -> Unit
) {
    OutlinedButton(
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor =
            if (selected)
                MaterialTheme.colorScheme.tertiaryContainer
            else
                Color.Transparent
        ),/*modifier = Modifier
            .background(
                if (selected)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    Color.Transparent
            ),*/
        onClick = { callback() }
    ) {
        Text(
            if (dataRange == TimerSessionViewModel.DataRange.ALL_TIME)
                "All time"
            else
                "${dataRange.asInt} days"
        )
    }
}


@Composable
fun SessionHistoryRow() {

}