package com.abelvolpi.mydailyschedule.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.abelvolpi.mydailyschedule.MainActivity
import com.abelvolpi.mydailyschedule.data.Task
import com.abelvolpi.mydailyschedule.data.TaskDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar

class SchedulerWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val tasks = withContext(Dispatchers.IO) {
            TaskDatabase.getDatabase(context).taskDao().getAllTasksSync()
        }

        val cal = Calendar.getInstance()
        val currentHour = cal.get(Calendar.HOUR_OF_DAY)
        val currentMinute = cal.get(Calendar.MINUTE)
        val currentMinutes = currentHour * 60 + currentMinute

        val remaining = tasks.filter { task ->
            (task.startHour * 60 + task.startMinute + task.durationMinutes) > currentMinutes
        }

        val currentTimeText = String.format("%02d:%02d", currentHour, currentMinute)

        provideContent {
            GlanceTheme {
                WidgetContent(
                    currentTime = currentTimeText,
                    tasks = remaining,
                    context = context
                )
            }
        }
    }
}

@SuppressLint("RestrictedApi")
@Composable
private fun WidgetContent(
    currentTime: String,
    tasks: List<Task>,
    context: Context
) {
    val openAppIntent = Intent(context, MainActivity::class.java)

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(ColorProvider(Color(0xFF1C1C1C)))
            .padding(12.dp)
            .clickable(actionStartActivity(openAppIntent))
    ) {
        // Header row: app name + current time
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "My Schedule",
                style = TextStyle(
                    color = ColorProvider(Color(0xFFD0BCFF)),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = GlanceModifier.defaultWeight()
            )
            Text(
                text = currentTime,
                style = TextStyle(
                    color = ColorProvider(Color(0xFFFF1744)),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Spacer(modifier = GlanceModifier.height(8.dp))

        if (tasks.isEmpty()) {
            Text(
                text = "All done for today! \uD83C\uDF89",
                style = TextStyle(
                    color = ColorProvider(Color(0xFF9E9E9E)),
                    fontSize = 13.sp
                )
            )
        } else {
            tasks.take(4).forEach { task ->
                TaskRow(task = task)
                Spacer(modifier = GlanceModifier.height(6.dp))
            }
            if (tasks.size > 4) {
                Text(
                    text = "+${tasks.size - 4} more tasks",
                    style = TextStyle(
                        color = ColorProvider(Color(0xFF9E9E9E)),
                        fontSize = 11.sp
                    )
                )
            }
        }
    }
}

@SuppressLint("RestrictedApi")
@Composable
private fun TaskRow(task: Task) {
    val taskColor = Color(android.graphics.Color.parseColor(task.colorHex))

    Row(
        modifier = GlanceModifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = GlanceModifier
                .size(10.dp)
                .background(ColorProvider(taskColor))
                .cornerRadius(5.dp),
            contentAlignment = Alignment.Center
        ) {}
        Spacer(modifier = GlanceModifier.width(6.dp))
        Text(
            text = "${String.format("%02d:%02d", task.startHour, task.startMinute)}  ${task.title}",
            style = TextStyle(
                color = ColorProvider(Color.White),
                fontSize = 12.sp
            ),
            maxLines = 1,
            modifier = GlanceModifier.defaultWeight()
        )
    }
}
