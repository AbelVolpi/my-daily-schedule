package com.abelvolpi.mydailyschedule.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abelvolpi.mydailyschedule.data.Task
import com.abelvolpi.mydailyschedule.data.TaskRepository
import kotlinx.coroutines.launch

class AddTaskViewModel(private val repository: TaskRepository) : ViewModel() {

    fun saveTask(
        title: String,
        description: String,
        startHour: Int,
        startMinute: Int,
        durationMinutes: Int,
        colorHex: String
    ) {
        viewModelScope.launch {
            repository.insertTask(
                Task(
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
}
