package com.example.notesapp.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notesapp.Models.Note
import com.example.notesapp.Utilities.DATABASE_NAME

// Database class for managing the notes database
@Database(entities = arrayOf(Note::class), version=1, exportSchema = false)
abstract class NotesDatabase:RoomDatabase() {

    // Abstract method to get the NoteDao
    abstract fun getNoteDao(): NoteDao

    // Companion object for database initialization
    companion object{

        @Volatile
        private var INSTANCE: NotesDatabase?=null

        // Method to get the database instance
        fun getDatabase(context: Context):NotesDatabase{

            // Return the existing instance if available, otherwise create a new one
            return INSTANCE?: synchronized(this){

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotesDatabase::class.java,
                    DATABASE_NAME
                ).build()

                INSTANCE = instance
                instance

            }

        }

    }

}