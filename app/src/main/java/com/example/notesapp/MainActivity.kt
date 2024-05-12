package com.example.notesapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notesapp.Adapter.NotesAdapter
import com.example.notesapp.Database.NotesDatabase
import com.example.notesapp.Models.Note
import com.example.notesapp.Models.NoteViewModel
import com.example.notesapp.databinding.ActivityMainBinding

// MainActivity class, the central activity of the notes app
class MainActivity : AppCompatActivity(), NotesAdapter.NotesClickListener , PopupMenu.OnMenuItemClickListener{

    // View binding instance
    private lateinit var binding: ActivityMainBinding
    // Database instance
    private lateinit var database: NotesDatabase
    // ViewModel instance
    lateinit var viewModel: NoteViewModel
    // Adapter for the RecyclerView
    lateinit var adapter: NotesAdapter
    // Selected note for long press actions
    lateinit var selectedNote: Note


    // Activity result launcher for updating a note
    private val updateNote = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
        if(result.resultCode==Activity.RESULT_OK){
            val note = result.data?.getSerializableExtra("note") as? Note
            if(note != null){
                viewModel.updateNote(note)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()


        viewModel = ViewModelProvider(this,
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NoteViewModel::class.java)


        // Observe changes in the list of notes
        viewModel.allnotes.observe(this) { list ->

            list?.let {

                adapter.updateList(list)

            }
        }

        database = NotesDatabase.getDatabase(this)


    }


    // Function to initialize UI elements
    private fun initUI() {

        // Set RecyclerView properties
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager  = StaggeredGridLayoutManager(2,LinearLayout.VERTICAL)
        adapter = NotesAdapter(this,this)
        binding.recyclerView.adapter = adapter

        // Activity result launcher for getting a new note
        val getContent  = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
            if(result.resultCode == Activity.RESULT_OK){
                val note = result.data?.getSerializableExtra("note") as? Note
                if(note!=null){
                    viewModel.insertNote(note)
                }
            }
        }


        // FAB click listener to add a new note
        binding.FABadd.setOnClickListener{
            val intent = Intent(this,AddNote::class.java)
            getContent.launch(intent)
        }

        // SearchView listener for filtering notes
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText!=null){
                    adapter.filterList(newText)
                }
                return true
            }

        })
    }


    // Click listener for a note item
    override fun onItemClicked(note: Note) {
        val intent = Intent(this,AddNote::class.java)
        intent.putExtra("current_note",note)
        updateNote.launch(intent)
    }

    // Long click listener for a note item
    override fun onLongItemClicked(note: Note, cardView: CardView) {
        selectedNote = note
        popUpDisplay(cardView)
    }

    // Function to display a pop-up menu for long press actions
    private fun popUpDisplay(cardView: CardView) {
        val popup = PopupMenu(this,cardView)
        popup.setOnMenuItemClickListener(this)
        popup.inflate(R.menu.pop_up_menu)
        popup.show()

    }

    // Function to handle pop-up menu item clicks
    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if(item?.itemId==R.id.delete_note){
            viewModel.deleteNote(selectedNote)
            return true
        }
        return false
    }

}