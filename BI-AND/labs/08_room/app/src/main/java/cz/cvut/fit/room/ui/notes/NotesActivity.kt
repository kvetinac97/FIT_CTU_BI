package cz.cvut.fit.room.ui.notes

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cz.cvut.fit.room.R
import cz.cvut.fit.room.model.Note
import cz.cvut.fit.room.ui.newnote.NewNoteActivity
import kotlinx.android.synthetic.main.activity_notes.*

class NotesActivity : AppCompatActivity() {

    private lateinit var adapter: NotesAdapter
    private lateinit var viewModel: NotesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)
        adapter = NotesAdapter { onNoteLongClicked(it) }
        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(this)

        // 6 observovat z viewmodelu seznam receptu a při změně aktualizovat adapter. Pokud
        // se vrátí prázdný seznam receptů, zobrazíme empty text.
        viewModel = ViewModelProvider(this).get(NotesViewModel::class.java)
        viewModel.observeNotes().observe(this, {
            adapter.setNotes(it)
            txt_empty.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
        })
    }

    private fun onNoteLongClicked(note: Note)
        = AlertDialog.Builder(this)
            .setTitle(R.string.notes_remove_dialog_title)
            .setMessage(R.string.notes_remove_dialog_message)
            .setPositiveButton(R.string.notes_remove_dialog_yes) { _, _ -> viewModel.deleteNote(note) }
            .setNegativeButton(R.string.notes_remove_dialog_no, null)
            .show()

    override fun onCreateOptionsMenu (menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_notes, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected (item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add)
            startActivity(Intent(this, NewNoteActivity::class.java))
        return super.onOptionsItemSelected(item)
    }

}
