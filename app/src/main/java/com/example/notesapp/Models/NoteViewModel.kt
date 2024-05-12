package com.example.notesapp.Models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.notesapp.Database.NotesDatabase
import com.example.notesapp.Database.NotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


// ViewModel class for managing UI-related data in a lifecycle-conscious way
class NoteViewModel(application: Application) : AndroidViewModel(application) {


    // Repository instance for accessing data
    private val repository: NotesRepository

    val allnotes: LiveData<List<Note>>


    // Initialization block to initialize the repository and LiveData
    init {
        // Accessing the DAO instance from the database
        val dao = NotesDatabase.getDatabase(application).getNoteDao()

        // Initializing the repository with the DAO instance
        repository = NotesRepository(dao)

        // Assigning LiveData from repository
        allnotes = repository.allNotes
    }

    // Function to delete a note
    fun deleteNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(note)
    }

    // Function to insert a note
    fun insertNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(note)
    }

    // Function to update a note
    fun updateNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(note)
    }
}