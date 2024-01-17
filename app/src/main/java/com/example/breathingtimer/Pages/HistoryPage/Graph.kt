package com.example.breathingtimer.Pages.HistoryPage

import android.graphics.Typeface
import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.LineType
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import com.example.breathingtimer.Home.BreathingSession
import com.example.breathingtimer.Home.TimerSessionViewModel
import java.text.SimpleDateFormat
import java.util.Date


class GraphBuilder(
    viewModel: TimerSessionViewModel
) {
    val sessionDatabase = viewModel.sessionDatabase
    var pointsData: List<Point>? = null //testPointsData
    var xAxisValues: List<String>? = null // pointsDataXAxisValues

    init {
        if (sessionDatabase.isNotEmpty()) {
            val rawPoints = getRawDataPoints(sessionDatabase)
            xAxisValues = getXAxisValues(rawPoints, 5)

            //Log.d("historyPage", "xAxisValues = $testPointsDataXAxisValues")
            val testPointsDataX = scaleLongsToFloats(rawPoints.map { it.first })
            val testPointsDataY = rawPoints.map { it.second.toFloat() }
            pointsData = testPointsDataX.zip(testPointsDataY) { x, y ->
                Point(x, y)
            }
        }
        //viewModel.generateSessionData()
    }

    /*val testPointsData = listOf(
        Point(0f, 234f),
        Point(2f, 3f),
        Point(3f, 0f),
        Point(4f, 34f),
        Point(5f, 25f),
        Point(6f, 23f),
        Point(7f, 123f),
        Point(10f, 178f),
    )

    val rawPointsData = getRawDataPoints(viewModel.sessionDatabase)

    */

    private fun getRawDataPoints(
        sessionDatabase: MutableList<BreathingSession>
    ): List<Pair<Long, Int>> {
        val rawPointsData = sessionDatabase.map { session ->
            Pair(session.timeStampMillis, session.averageHoldTimeSecs)
        }
        return rawPointsData
    }

    private fun timestampToDDMM(
        timestampMillis: Long
    ): String {
        val sdf = SimpleDateFormat("dd/MM")
        val date = Date(timestampMillis)
        return sdf.format(date)
    }

    // get values for xAxis before timestamp<Long> is scaled to 0f..10f
    private fun getXAxisValues(
        rawList: List<Pair<Long, Int>>,
        steps: Int
    ): List<String> {
        val xAxisValues: MutableList<String> = mutableListOf()
        val minX = rawList.minBy { it.first }.first
        val maxX = rawList.maxBy { it.first }.first
        val rangeX = maxX - minX

        val increment = rangeX / steps

        for (i in 0 until steps) {
            val value = timestampToDDMM(minX + (i * increment))
            xAxisValues.add(value)
        }
        return xAxisValues
    }

    // scale timestamp<Long> to 0f..10f
    private fun scaleLongsToFloats(
        longs: List<Long>
    ): List<Float> {
        if (longs.isEmpty()) {
            return emptyList()
        }

        val minLong = longs.minOrNull() ?: 0L
        val maxLong = longs.maxOrNull() ?: 0L

        val scaleFactor = 10f / (maxLong - minLong)

        return longs.map { long ->
            ((long - minLong) * scaleFactor)
        }
    }

    private fun getAxesRanges(
        pointsData: List<Point>
    ): Pair<IntRange, IntRange> {
        val minX = pointsData.minBy { it.x }.x.toInt()
        val maxX = pointsData.maxBy { it.x }.x.toInt()
        val minY = pointsData.minBy { it.y }.y.toInt()
        val maxY = pointsData.maxBy { it.y }.y.toInt()

        val xRange = minX..maxX
        val yRange = minY..maxY

        return Pair(xRange, yRange)
    }

    @Composable
    fun BreevLinechart() {
        if (pointsData == null || xAxisValues == null)
            return
        val axesRanges = getAxesRanges(pointsData!!)
        val xAxisRange = axesRanges.first
        val yAxisRange = axesRanges.second

        val stepsX = 5
        val stepsY = 5

        val xAxisData = AxisData.Builder()
            //.axisStepSize(1.dp)
            .steps(stepsX)
            .labelData { i ->
                //xAxisValues[i]
                if (i == 0)
                    ""
                else {
                    Log.d("xAxisData", "i = $i, xAxisValues = $xAxisValues")
                    xAxisValues!![(i / 2) - 1]
                }
            }
            //.axisLabelAngle(20f)
            .labelAndAxisLinePadding(15.dp)
            .axisLabelColor(Color.Blue)
            .axisLineColor(Color.DarkGray)
            .typeFace(Typeface.DEFAULT)
            .build()
        val yAxisData = AxisData.Builder()
            .steps(stepsY)
            .labelData { i ->
                if (i == 0)
                    ""
                else {
                    val yRange = yAxisRange.last - yAxisRange.first
                    Log.d(
                        "yAxisData",
                        "i = $i, (i * (yAxisRange.last / stepsY)) + yAxisRange.first = ${(i * (yAxisRange.last / stepsY)) + yAxisRange.first}"
                    )
                    "${(i * (yRange / stepsY)) + yAxisRange.first}"
                }

            }
            .labelAndAxisLinePadding(30.dp)
            .axisLabelColor(Color.Blue)
            .axisLineColor(Color.DarkGray)
            .typeFace(Typeface.DEFAULT_BOLD)
            .build()
        val data = LineChartData(
            linePlotData = LinePlotData(
                lines = listOf(
                    Line(
                        dataPoints = pointsData!!,
                        lineStyle = LineStyle(
                            lineType = LineType.Straight(),
                            color = Color.Black
                        ),
                        intersectionPoint = IntersectionPoint(
                            color = Color.Black,
                            radius = 3.dp
                        ),
                        selectionHighlightPopUp = SelectionHighlightPopUp(popUpLabel = { x, y ->
                            val xLabel = ""
                            val yLabel = "best time : ${y.toInt()}s"
                            "$xLabel\n$yLabel"
                        })
                    ),
                    //dummy line to set range
                    /*Line(
                    dataPoints = pointsData.subList(4, 8),
                    lineStyle = LineStyle(
                        lineType = LineType.Straight(),
                        color = Color.Transparent
                    ),
                    intersectionPoint = IntersectionPoint(
                        color = Color.Transparent,
                        radius = 3.dp
                    )
                )*/
                )
            ),
            xAxisData = xAxisData,
            yAxisData = yAxisData,
            isZoomAllowed = false
        )
        LineChart(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            lineChartData = data
        )
    }

    /*@Composable
    fun PointsDataHead() {
        for (i in pointsData.indices) {
            Text("pointsData[$i]: x = ${pointsData[i].x}, y = ${pointsData[i].y}")
        }
    }*/


}

