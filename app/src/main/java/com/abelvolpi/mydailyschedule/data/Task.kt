package com.abelvolpi.mydailyschedule.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String = "",
    val startHour: Int,       // 0–23
    val startMinute: Int,     // 0 or 30
    val durationMinutes: Int, // 15, 30, 45, 60, 90, 120
    val colorHex: String      // e.g. "#7C4DFF"
)
