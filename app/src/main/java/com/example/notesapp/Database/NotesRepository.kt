package com.example.notesapp.Database

import androidx.lifecycle.LiveData
import com.example.notesapp.Models.Note

// Repository class for managing interactions between ViewModel and Database
class NotesRepository(private val noteDao: NoteDao) {
    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes() // Update property to non-nullable LiveData


    // Method to insert a note into the database
    suspend fun insert(note: Note) {
        noteDao.insert(note)
    }

    // Method to delete a note from the database
    suspend fun delete(note: Note) {
        noteDao.delete(note)
    }

    // Method to update a note in the databas
    suspend fun update(note: Note) {
        // Update the note in the database and get the number of rows affected
        val rowsUpdated = note.id?.let { note.title?.let { it1 ->
            note.note?.let { it2 ->
                noteDao.update(it,
                    it1, it2
                )
            }
        } }
        if (rowsUpdated == 0) {
            // Handle accordingly, maybe throw an exception or log a message
        }
    }
}