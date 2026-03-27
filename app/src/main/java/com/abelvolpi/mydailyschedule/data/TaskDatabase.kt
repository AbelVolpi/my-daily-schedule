package com.abelvolpi.mydailyschedule.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: TaskDatabase? = null

        fun getDatabase(context: Context): TaskDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    "task_database"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            INSTANCE?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    seedDatabase(database.taskDao())
                                }
                            }
                        }
                    })
                    .build()
                    .also { INSTANCE = it }
            }
        }

        private suspend fun seedDatabase(dao: TaskDao) {
            listOf(
                Task(title = "Morning Workout", description = "Cardio + stretching", startHour = 7, startMinute = 0, durationMinutes = 60, colorHex = "#FF6B6B"),
                Task(title = "Breakfast", description = "Healthy start to the day", startHour = 8, startMinute = 0, durationMinutes = 30, colorHex = "#FFA94D"),
                Task(title = "Deep Work", description = "Focus on high-priority tasks", startHour = 9, startMinute = 0, durationMinutes = 120, colorHex = "#7C4DFF"),
                Task(title = "Lunch Break", description = "Step away from the screen", startHour = 12, startMinute = 0, durationMinutes = 60, colorHex = "#51CF66"),
                Task(title = "Emails & Messages", description = "Catch up on communications", startHour = 14, startMinute = 0, durationMinutes = 30, colorHex = "#339AF0"),
                Task(title = "Evening Walk", description = "Fresh air and movement", startHour = 18, startMinute = 0, durationMinutes = 30, colorHex = "#20C997"),
                Task(title = "Dinner", description = "Family dinner time", startHour = 19, startMinute = 0, durationMinutes = 60, colorHex = "#F06595"),
                Task(title = "Reading", description = "Books or articles", startHour = 21, startMinute = 0, durationMinutes = 60, colorHex = "#A9E34B"),
            ).forEach { dao.insert(it) }
        }
    }
}
