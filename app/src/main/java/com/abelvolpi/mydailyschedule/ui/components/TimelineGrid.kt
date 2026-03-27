package com.abelvolpi.mydailyschedule.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.abelvolpi.mydailyschedule.ui.theme.GridLine
import com.abelvolpi.mydailyschedule.ui.theme.GridLineMajor
import com.abelvolpi.mydailyschedule.ui.theme.TimeLabel

@Composable
fun TimelineGrid(
    slotHeight: Dp,
    timeLabelWidth: Dp,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        // Grid lines drawn on the right side
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = timeLabelWidth)
        ) {
            val slotPx = slotHeight.toPx()
            repeat(49) { slot ->
                val y = slot * slotPx
                val isHour = slot % 2 == 0
                drawLine(
                    color = if (isHour) GridLineMajor else GridLine,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = if (isHour) 1.dp.toPx() else 0.5.dp.toPx()
                )
            }
        }

        // Time labels column
        androidx.compose.foundation.layout.Column(
            modifier = Modifier
                .width(timeLabelWidth)
                .fillMaxHeight()
        ) {
            repeat(48) { slot ->
                val hour = slot / 2
                val minute = (slot % 2) * 30
                Box(
                    modifier = Modifier
                        .width(timeLabelWidth)
                        .weight(1f)
                        .padding(end = 6.dp),
                    contentAlignment = Alignment.TopEnd
                ) {
                    Text(
                        text = String.format("%02d:%02d", hour, minute),
                        color = TimeLabel,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}
