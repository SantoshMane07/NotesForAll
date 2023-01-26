package com.example.firebaseone

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.rows.view.*

class NotesAdapter(private val listener:iNotesAdapter) : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    lateinit var arrNotes:ArrayList<NotesModel>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val viewholder=NotesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rows,parent,false))

        // on click listener for update a note
        viewholder.btn_update_note.setOnClickListener{
            listener.updateNote(arrNotes[viewholder.adapterPosition])
        }
        // on click listener for delete a note
        viewholder.btn_delete_note.setOnClickListener {
            listener.deleteNote(arrNotes[viewholder.adapterPosition])
        }

        return viewholder
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.tv_title.text=arrNotes[position].title
        holder.tv_description.text=arrNotes[position].description
    }

    override fun getItemCount(): Int {
         return arrNotes.size
    }

    class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tv_title: TextView = itemView.tv_title
        val tv_description :TextView = itemView.tv_description
        val btn_update_note :Button = itemView.btn_update_note
        val btn_delete_note :Button = itemView.btn_delete_note
    }
    //update all Notes on Change observed
    fun updateNotes(newArrNotes: ArrayList<NotesModel>){
        arrNotes = ArrayList<NotesModel>()
        arrNotes.clear()
        arrNotes.addAll(newArrNotes)
        notifyDataSetChanged()
    }
}
//Interface for on click Listener
interface iNotesAdapter{
    fun deleteNote(note: NotesModel)
    fun updateNote(note: NotesModel)
}
