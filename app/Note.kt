package com.example.mynotes.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(), // Поле timestamp
    val color: Int = 0xFF6200EE.toInt() // Цвет карточки по умолчанию
)
