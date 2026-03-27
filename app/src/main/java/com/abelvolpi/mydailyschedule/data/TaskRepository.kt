package com.abelvolpi.mydailyschedule.data

import kotlinx.coroutines.flow.Flow

class TaskRepository(private val dao: TaskDao) {

    fun getAllTasks(): Flow<List<Task>> = dao.getAllTasks()

    suspend fun getAllTasksSync(): List<Task> = dao.getAllTasksSync()

    suspend fun getTaskById(id: Int): Task? = dao.getTaskById(id)

    suspend fun insertTask(task: Task) = dao.insert(task)

    suspend fun updateTask(task: Task) = dao.update(task)

    suspend fun deleteTask(task: Task) = dao.delete(task)
}
