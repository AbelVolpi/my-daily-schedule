package com.abelvolpi.mydailyschedule.di

import com.abelvolpi.mydailyschedule.data.TaskDatabase
import com.abelvolpi.mydailyschedule.data.TaskRepository
import com.abelvolpi.mydailyschedule.ui.add.AddTaskViewModel
import com.abelvolpi.mydailyschedule.ui.edit.EditTaskViewModel
import com.abelvolpi.mydailyschedule.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { TaskDatabase.getDatabase(get()) }
    single { get<TaskDatabase>().taskDao() }
    single { TaskRepository(get()) }

    viewModel { MainViewModel(get()) }
    viewModel { AddTaskViewModel(get()) }
    viewModel { params -> EditTaskViewModel(get(), params.get()) }
}
