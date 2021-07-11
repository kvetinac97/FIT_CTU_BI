package cz.cvut.fit.room.ui.notes

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cz.cvut.fit.room.R
import cz.cvut.fit.room.model.Note

/** ViewHolder of note item */
class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val txtTitle: TextView = itemView.findViewById(R.id.txt_title)
    private val txtDescription: TextView = itemView.findViewById(R.id.txt_description)

    fun bindNote (note: Note, onLongClickListener: (Note) -> Unit) {
        txtTitle.text = note.title
        txtDescription.text = note.description
        itemView.setOnLongClickListener {
            onLongClickListener(note)
            true
        }
    }

}
