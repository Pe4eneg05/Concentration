package com.pechenegmobilecompanyltd.concentration.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.pechenegmobilecompanyltd.concentration.data.model.ProductivityTrend

@Composable
fun ProductivityChart(
    data: ProductivityTrend,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            if (data.dates.isNotEmpty() && data.scores.isNotEmpty()) {
                drawChart(data.scores)
            }
        }
    }
}

private fun DrawScope.drawChart(scores: List<Float>) {
    val width = size.width
    val height = size.height
    val padding = 16.dp.toPx()

    val chartWidth = width - 2 * padding
    val chartHeight = height - 2 * padding

    // Draw grid lines
    val gridColor = Color.LightGray.copy(alpha = 0.3f)

    // Horizontal grid lines
    for (i in 0..4) {
        val y = padding + (chartHeight / 4) * i
        drawLine(
            color = gridColor,
            start = Offset(padding, y),
            end = Offset(width - padding, y),
            strokeWidth = 1.dp.toPx()
        )
    }

    // Draw data line
    if (scores.size > 1) {
        val linePath = Path()
        val pointRadius = 3.dp.toPx()
        val lineColor = Color.Blue
        val pointColor = Color.Blue

        for (i in scores.indices) {
            val x = padding + (chartWidth / (scores.size - 1)) * i
            val y = padding + chartHeight - (scores[i] * chartHeight)

            if (i == 0) {
                linePath.moveTo(x, y)
            } else {
                linePath.lineTo(x, y)
            }

            drawCircle(
                color = pointColor,
                center = Offset(x, y),
                radius = pointRadius
            )
        }

        drawPath(
            path = linePath,
            color = lineColor,
            style = Stroke(width = 2.dp.toPx())
        )
    }
}