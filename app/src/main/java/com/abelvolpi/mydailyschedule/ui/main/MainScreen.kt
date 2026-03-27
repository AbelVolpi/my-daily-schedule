package com.abelvolpi.mydailyschedule.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.abelvolpi.mydailyschedule.data.Task
import com.abelvolpi.mydailyschedule.ui.components.CurrentTimeLine
import com.abelvolpi.mydailyschedule.ui.components.TaskCard
import com.abelvolpi.mydailyschedule.ui.components.TimelineGrid
import com.abelvolpi.mydailyschedule.ui.theme.DarkBackground
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import java.util.Calendar

private val SLOT_HEIGHT = 64.dp
private val TIME_LABEL_WIDTH = 64.dp
private const val TOTAL_SLOTS = 48

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onAddTask: () -> Unit,
    onEditTask: (Int) -> Unit,
    viewModel: MainViewModel = koinViewModel()
) {
    val tasks by viewModel.tasks.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Daily Schedule") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground,
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTask,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        },
        containerColor = DarkBackground
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            AnimatedVisibility(
                visible = tasks.isEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                EmptyState(modifier = Modifier.fillMaxSize())
            }

            AnimatedVisibility(
                visible = tasks.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Timeline(
                    tasks = tasks,
                    onTaskClick = onEditTask,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun Timeline(
    tasks: List<Task>,
    onTaskClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val density = LocalDensity.current
    val totalHeight = SLOT_HEIGHT * TOTAL_SLOTS

    fun currentMinutesOfDay(): Int {
        val cal = Calendar.getInstance()
        return cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)
    }

    var currentMinutes by remember { mutableIntStateOf(currentMinutesOfDay()) }

    // Scroll to show current time near top of viewport on first launch
    LaunchedEffect(Unit) {
        val scrollPx = with(density) {
            (currentMinutes / 30f * SLOT_HEIGHT.toPx()).toInt()
        }
        scrollState.scrollTo((scrollPx - 300).coerceAtLeast(0))
    }

    // Tick every minute to update the current-time indicator
    LaunchedEffect(Unit) {
        while (true) {
            delay(60_000L)
            currentMinutes = currentMinutesOfDay()
        }
    }

    Box(modifier = modifier.verticalScroll(scrollState)) {
        // Fixed-height container so the scroll knows the full extent
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(totalHeight)
        ) {
            // Background grid + time labels
            TimelineGrid(
                slotHeight = SLOT_HEIGHT,
                timeLabelWidth = TIME_LABEL_WIDTH,
                modifier = Modifier.fillMaxSize()
            )

            // Task cards — each absolutely positioned by its start time
            tasks.forEach { task ->
                val topOffset = SLOT_HEIGHT * (task.startHour * 60 + task.startMinute) / 30f
                val taskHeight = SLOT_HEIGHT * task.durationMinutes / 30f

                Box(
                    modifier = Modifier
                        .absoluteOffset(y = topOffset)
                        .fillMaxWidth()
                        .height(taskHeight)
                        .padding(start = TIME_LABEL_WIDTH + 4.dp, end = 8.dp, top = 2.dp, bottom = 2.dp)
                ) {
                    TaskCard(
                        task = task,
                        modifier = Modifier.fillMaxSize(),
                        onClick = { onTaskClick(task.id) }
                    )
                }
            }

            // Red current-time indicator
            val currentTopOffset = SLOT_HEIGHT * currentMinutes / 30f
            CurrentTimeLine(
                modifier = Modifier
                    .absoluteOffset(y = currentTopOffset - 6.dp)
                    .fillMaxWidth()
                    .height(12.dp)
                    .padding(start = TIME_LABEL_WIDTH - 6.dp)
            )
        }
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "\uD83D\uDCC5",
            style = MaterialTheme.typography.displayLarge
        )
        Text(
            text = "No tasks yet",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "Tap + to add your first daily routine",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
