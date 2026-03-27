package com.abelvolpi.mydailyschedule.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CurrentTimeLine(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val circleRadius = 5.dp.toPx()
        val lineY = size.height / 2f
        val lineColor = Color(0xFFFF1744)

        drawCircle(
            color = lineColor,
            radius = circleRadius,
            center = Offset(circleRadius, lineY)
        )
        drawLine(
            color = lineColor,
            start = Offset(circleRadius * 2, lineY),
            end = Offset(size.width, lineY),
            strokeWidth = 2.dp.toPx()
        )
    }
}
