package com.example.notesapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.notesapp.Models.Note
import com.example.notesapp.databinding.ActivityAddNoteBinding
import java.text.SimpleDateFormat
import java.util.*

// Activity for adding or editing a note
class AddNote : AppCompatActivity() {

    // View binding for the activity layout
    private lateinit var binding: ActivityAddNoteBinding

    private lateinit var note: Note
    private lateinit var old_note: Note
    var isUpdate =false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try{
            // Attempt to retrieve the existing note data if available
            old_note = intent.getSerializableExtra("current_note") as Note

            // Set the title and note text fields with existing data for editing
            binding.ETtitle.setText(old_note.title)
            binding.ETnote.setText(old_note.note)
            // Set the update flag to true
            isUpdate =true
        }catch (e:Exception){
            e.printStackTrace()
        }

        // Click listener for the check button
        binding.IVcheck.setOnClickListener{
            val title = binding.ETtitle.text.toString()
            val note_desc = binding.ETnote.text.toString()
            if(title.isNotEmpty() || note_desc.isNotEmpty()){
                // Format the date
                val formatter = SimpleDateFormat("EEE, d MMM yyyy HH:mm a")

                if(isUpdate){
                    // Create a new Note object with the updated data
                    note = Note(
                        old_note.id,title,note_desc,formatter.format(Date())
                    )
                }else{
                    note = Note(
                        null,title,note_desc,formatter.format(Date())
                    )
                }

                // Prepare and send the result back to the calling activity
                val intent = Intent()
                intent.putExtra("note",note)
                setResult(Activity.RESULT_OK,intent)
                finish()

            }else{
                // Show a toast message if title or note description is empty
                Toast.makeText(this, "Please enter some data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }

        // Click listener for the back arrow button
        binding.IVbackarrow.setOnClickListener { onBackPressed() }
    }
}