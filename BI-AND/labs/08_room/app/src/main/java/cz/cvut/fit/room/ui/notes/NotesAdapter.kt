package cz.cvut.fit.room.ui.notes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cz.cvut.fit.room.R
import cz.cvut.fit.room.model.Note
import java.util.*

/**
 * RecyclerView adapter of notes list
 */
class NotesAdapter (private val onLongClickListener: (Note) -> Unit) : RecyclerView.Adapter<NoteViewHolder>() {

    private var notes: List<Note> = ArrayList()

    fun setNotes (notes: List<Note>) {
        this.notes = notes
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder (parent: ViewGroup, viewType: Int)
        = NoteViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false))

    override fun onBindViewHolder (holder: NoteViewHolder, position: Int)
        = holder.bindNote(notes[position], onLongClickListener)

    override fun getItemCount() = notes.size

}
