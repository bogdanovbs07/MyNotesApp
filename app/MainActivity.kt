package com.example.mynotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mynotes.data.NoteDatabase
import com.example.mynotes.ui.screens.NoteDetailScreen
import com.example.mynotes.ui.screens.NoteListScreen
import com.example.mynotes.ui.theme.MyNotesTheme
import com.example.mynotes.viewmodel.NoteViewModel
import com.example.mynotes.viewmodel.NoteViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val database = NoteDatabase.getDatabase(this)
        val viewModel: NoteViewModel by viewModels {
            NoteViewModelFactory(database.noteDao())
        }
        
        setContent {
            MyNotesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyNotesApp(viewModel)
                }
            }
        }
    }
}

@Composable
fun MyNotesApp(viewModel: NoteViewModel) {
    val navController = rememberNavController()
    val notes by viewModel.notes.collectAsState()
    
    // Задание 1: Изменяемые цвета
    var backgroundColor by remember { mutableStateOf(Color.White) }
    var buttonColor by remember { mutableStateOf(Color(0xFF6200EE)) }
    
    NavHost(navController = navController, startDestination = "notes_list") {
        composable("notes_list") {
            NoteListScreen(
                notes = notes,
                onAddNote = { title, content ->
                    viewModel.addNote(title, content)
                },
                onDeleteNote = { note ->
                    viewModel.deleteNote(note)
                },
                onNoteClick = { noteId ->
                    navController.navigate("note_detail/$noteId")
                },
                backgroundColor = backgroundColor,
                buttonColor = buttonColor
            )
        }
        composable("note_detail/{noteId}") { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")?.toIntOrNull() ?: 0
            NoteDetailScreen(
                noteId = noteId,
                getNoteById = { id, callback ->
                    viewModel.getNoteById(id, callback)
                },
                onDeleteNote = { note ->
                    viewModel.deleteNote(note)
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
