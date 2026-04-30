package com.example.mynotes.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mynotes.data.Note
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    noteId: Int,
    getNoteById: (Int, (Note?) -> Unit) -> Unit,
    onDeleteNote: (Note) -> Unit,
    onNavigateBack: () -> Unit
) {
    var note by remember { mutableStateOf<Note?>(null) }
    
    LaunchedEffect(noteId) {
        // Вывод в консоль ID заметки (задание 4)
        Log.d("NoteApp", "Открыт экран деталей заметки с ID: $noteId")
        
        getNoteById(noteId) { foundNote ->
            note = foundNote
            if (foundNote != null) {
                Log.d("NoteApp", "Найдена заметка: ${foundNote.title}")
            }
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(note?.title ?: "Заметка") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            note?.let {
                                onDeleteNote(it)
                                Log.d("NoteApp", "Удаление заметки с ID: ${it.id} из деталей")
                                onNavigateBack()
                            }
                        }
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Удалить")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (note != null) {
                val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                val formattedDate = dateFormat.format(Date(note!!.timestamp))
                
                Text(
                    text = "Дата создания: $formattedDate",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = note!!.content,
                    fontSize = 16.sp
                )
            }
        }
    }
}
