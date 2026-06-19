package com.example.todolist

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String?,
    val priority: Int, // 1: Low, 2: Medium, 3: High
    val dueDate: Long?,
    val isCompleted: Boolean = false
)
