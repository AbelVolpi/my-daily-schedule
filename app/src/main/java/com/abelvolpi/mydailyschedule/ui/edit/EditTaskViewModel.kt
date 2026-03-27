package com.abelvolpi.mydailyschedule.ui.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abelvolpi.mydailyschedule.data.Task
import com.abelvolpi.mydailyschedule.data.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EditTaskViewModel(
    private val repository: TaskRepository,
    private val taskId: Int
) : ViewModel() {

    private val _task = MutableStateFlow<Task?>(null)
    val task: StateFlow<Task?> = _task.asStateFlow()

    init {
        viewModelScope.launch {
            _task.value = repository.getTaskById(taskId)
        }
    }

    fun updateTask(
        title: String,
        description: String,
        startHour: Int,
        startMinute: Int,
        durationMinutes: Int,
        colorHex: String
    ) {
        val current = _task.value ?: return
        viewModelScope.launch {
            repository.updateTask(
                current.copy(
                    title = title.trim(),
                    description = description.trim(),
                    startHour = startHour,
                    startMinute = startMinute,
                    durationMinutes = durationMinutes,
                    colorHex = colorHex
                )
            )
        }
    }

    fun deleteTask() {
        val current = _task.value ?: return
        viewModelScope.launch {
            repository.deleteTask(current)
        }
    }
}
