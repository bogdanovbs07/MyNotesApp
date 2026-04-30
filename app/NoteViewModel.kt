package com.example.mynotes.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mynotes.data.Note
import com.example.mynotes.data.NoteDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NoteViewModel(private val noteDao: NoteDao) : ViewModel() {
    
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()
    
    init {
        viewModelScope.launch {
            noteDao.getAllNotes().collect { notesList ->
                _notes.value = notesList
            }
        }
    }
    
    fun addNote(title: String, content: String) {
        viewModelScope.launch {
            val note = Note(
                title = title,
                content = content,
                timestamp = System.currentTimeMillis()
            )
            noteDao.insertNote(note)
            // Вывод в консоль текста из поля ввода (задание 2)
            Log.d("NoteApp", "Добавлена заметка: $title")
            Log.d("NoteApp", "Содержание: $content")
        }
    }
    
    fun deleteNote(note: Note) {
        viewModelScope.launch {
            noteDao.deleteNote(note)
            // Вывод в консоль при удалении (задание 5)
            Log.d("NoteApp", "Удалена заметка с ID: ${note.id}")
        }
    }
    
    fun getNoteById(id: Int, callback: (Note?) -> Unit) {
        viewModelScope.launch {
            val note = noteDao.getNoteById(id)
            // Вывод в консоль ID заметки (задание 4)
            Log.d("NoteApp", "Получена заметка с ID: $id")
            callback(note)
        }
    }
}

class NoteViewModelFactory(private val noteDao: NoteDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteViewModel(noteDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
