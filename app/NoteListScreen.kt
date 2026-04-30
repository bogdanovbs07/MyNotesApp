package com.example.mynotes.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mynotes.data.Note
import com.example.mynotes.ui.components.NoteCard
import com.example.mynotes.ui.theme.NoteColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteListScreen(
    notes: List<Note>,
    onAddNote: (String, String) -> Unit,
    onDeleteNote: (Note) -> Unit,
    onNoteClick: (Int) -> Unit,
    // Параметры для изменения цвета (задание 1)
    backgroundColor: Color = Color.White,
    buttonColor: Color = Color(0xFF6200EE)
) {
    var showDialog by remember { mutableStateOf(false) }
    var noteToDelete by remember { mutableStateOf<Note?>(null) }
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    
    // Диалог с подтверждением удаления (задание 7)
    if (showDialog && noteToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
                noteToDelete = null
            },
            title = { Text("Удаление заметки") },
            text = { Text("Вы уверены, что хотите удалить заметку \"${noteToDelete?.title}\"?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        noteToDelete?.let {
                            onDeleteNote(it)
                            Log.d("NoteApp", "Подтверждено удаление заметки с ID: ${it.id}")
                        }
                        showDialog = false
                        noteToDelete = null
                    }
                ) {
                    Text("Удалить", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        noteToDelete = null
                    }
                ) {
                    Text("Отмена")
                }
            }
        )
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Мои заметки") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (title.isNotBlank() && content.isNotBlank()) {
                        onAddNote(title, content)
                        // Вывод в консоль текста из поля ввода (задание 2)
                        Log.d("NoteApp", "Текст из поля ввода - Заголовок: $title")
                        Log.d("NoteApp", "Текст из поля ввода - Содержание: $content")
                        title = ""
                        content = ""
                    }
                },
                containerColor = buttonColor // Изменение цвета кнопки (задание 1)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Добавить")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(backgroundColor) // Изменение цвета фона (задание 1)
        ) {
            // Поля ввода
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Заголовок") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Содержание") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            
            // Список заметок
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(notes) { note ->
                    NoteCard(
                        note = note,
                        onClick = { onNoteClick(note.id) }
                    )
                }
            }
        }
    }
}
