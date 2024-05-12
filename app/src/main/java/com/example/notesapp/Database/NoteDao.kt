package com.example.notesapp.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.notesapp.Models.Note


// Dao interface for Note entity
@Dao
interface NoteDao {
    // Insert method for adding a new note
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: Note)

    // Delete method for removing a note
    @Delete
    fun delete(note: Note)

    // Query method to get all notes from the database
    @Query("SELECT * FROM notes_table ORDER BY id ASC")
    fun getAllNotes(): LiveData<List<Note>> // Update return type to non-nullable LiveData

    // Query method to update a note in the database
    @Query("UPDATE notes_table SET title = :title, note = :note WHERE id = :id")
    fun update(id: Int, title: String, note: String): Int // Update parameter and return types to non-nullable
}