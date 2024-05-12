package com.example.notesapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.Models.Note
import com.example.notesapp.R
import kotlin.random.Random

// Adapter class for the RecyclerView displaying notes
class NotesAdapter(private val context: Context,val listener:NotesClickListener) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    // List to hold the displayed notes
    private val NotesList= ArrayList<Note>()

    // List to hold all notes (used for filtering)
    private val fullList= ArrayList<Note>()


    // Inflates the layout for each note item in the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(context).inflate(R.layout.list_item,parent,false)
        )
    }

    // Returns the number of notes in the list
    override fun getItemCount(): Int {
        return NotesList.size
    }

    // Binds the data to the ViewHolder
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = NotesList[position]
        holder.title.text = currentNote.title
        holder.title.isSelected=true

        holder.note.text = currentNote.note
        holder.date.text = currentNote.date
        holder.date.isSelected=true



        // Sets a random background color for each note card
        holder.notes_layout.setCardBackgroundColor(holder.itemView.resources.getColor(randomColor()))

        // Sets click and long click listeners for each note card
        holder.notes_layout.setOnClickListener {
            listener.onItemClicked(NotesList[holder.adapterPosition])
        }

        holder.notes_layout.setOnLongClickListener {
            listener.onLongItemClicked(NotesList[holder.adapterPosition],holder.notes_layout)
            true
        }
    }

    // Updates the list of notes
    fun updateList(newList: List<Note>){
        fullList.clear()
        fullList.addAll(newList)

        NotesList.clear()
        NotesList.addAll(fullList)

        notifyDataSetChanged()
    }

    // Filters the list of notes based on a search query
    fun filterList(search:String){
        NotesList.clear()
        for(item in fullList){
            if(item.title?.lowercase()?.contains(search.lowercase())==true || item.note?.lowercase()?.contains(search.lowercase())==true){
                NotesList.add(item)
            }
        }

        notifyDataSetChanged()
    }

    // Generates a random color for the background of note cards
    fun randomColor() : Int{
        val list = ArrayList<Int>()
        list.add(R.color.NoteColor1)
        list.add(R.color.NoteColor2)
        list.add(R.color.NoteColor3)
        list.add(R.color.NoteColor4)
        list.add(R.color.NoteColor5)
        list.add(R.color.NoteColor6)

        val seed = System.currentTimeMillis().toInt()
        val randomIndex = Random(seed).nextInt(list.size)
        return list[randomIndex]
    }


    // ViewHolder class for holding the views of each note ite
    inner class NoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val notes_layout = itemView.findViewById<CardView>(R.id.card_layout)
        val title = itemView.findViewById<TextView>(R.id.TVtitle)
        val note = itemView.findViewById<TextView>(R.id.TVnote)
        val date = itemView.findViewById<TextView>(R.id.TVdate)


    }

    // Interface for click and long click events on note items
    interface NotesClickListener{
        fun onItemClicked(note:Note)
        fun onLongItemClicked(note:Note, cardView: CardView)
    }

}