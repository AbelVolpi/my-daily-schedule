package com.abelvolpi.mydailyschedule.ui.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.abelvolpi.mydailyschedule.ui.add.ColorPicker
import com.abelvolpi.mydailyschedule.ui.add.HourDropdown
import com.abelvolpi.mydailyschedule.ui.add.MinuteDropdown
import com.abelvolpi.mydailyschedule.ui.add.DurationSelector
import com.abelvolpi.mydailyschedule.ui.theme.DarkBackground
import com.abelvolpi.mydailyschedule.ui.theme.taskColorOptions
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskScreen(
    taskId: Int,
    onNavigateBack: () -> Unit,
    viewModel: EditTaskViewModel = koinViewModel(parameters = { parametersOf(taskId) })
) {
    val task by viewModel.task.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Form state — initialised once the task loads from the DB
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedHour by remember { mutableIntStateOf(0) }
    var selectedMinute by remember { mutableIntStateOf(0) }
    var selectedDuration by remember { mutableIntStateOf(30) }
    var selectedColor by remember { mutableStateOf(taskColorOptions.first()) }
    var formInitialised by remember { mutableStateOf(false) }

    LaunchedEffect(task) {
        if (!formInitialised && task != null) {
            task!!.let {
                title = it.title
                description = it.description
                selectedHour = it.startHour
                selectedMinute = it.startMinute
                selectedDuration = it.durationMinutes
                selectedColor = it.colorHex
            }
            formInitialised = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Task") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
            )
        },
        containerColor = DarkBackground
    ) { innerPadding ->
        if (!formInitialised) {
            // Loading state
            Column(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Spacer(Modifier.height(4.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description (optional)") },
                    minLines = 2,
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )

                Column {
                    Text(
                        text = "Start time",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Spacer(Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        HourDropdown(
                            selectedHour = selectedHour,
                            onHourSelected = { selectedHour = it },
                            modifier = Modifier.weight(1f)
                        )
                        MinuteDropdown(
                            selectedMinute = selectedMinute,
                            onMinuteSelected = { selectedMinute = it },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Column {
                    Text(
                        text = "Duration",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Spacer(Modifier.height(6.dp))
                    DurationSelector(
                        selectedDuration = selectedDuration,
                        onDurationSelected = { selectedDuration = it }
                    )
                }

                Column {
                    Text(
                        text = "Color",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Spacer(Modifier.height(6.dp))
                    ColorPicker(
                        selectedColor = selectedColor,
                        onColorSelected = { selectedColor = it }
                    )
                }

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            viewModel.updateTask(
                                title = title,
                                description = description,
                                startHour = selectedHour,
                                startMinute = selectedMinute,
                                durationMinutes = selectedDuration,
                                colorHex = selectedColor
                            )
                            onNavigateBack()
                        },
                        enabled = title.isNotBlank(),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Update")
                    }
                }

                Spacer(Modifier.height(16.dp))
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete task?") },
            text = { Text("\"${task?.title}\" will be permanently removed from your schedule.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteTask()
                        showDeleteDialog = false
                        onNavigateBack()
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFFF5252))
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
