package cz.cvut.fit.room.ui.newnote

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import cz.cvut.fit.room.R
import kotlinx.android.synthetic.main.activity_new_note.*

/** Activity with form for creating a new note */
class NewNoteActivity : AppCompatActivity() {

    lateinit var viewModel: NewNoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_note)
        setTitle(R.string.new_note_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewModel = ViewModelProvider(this).get(NewNoteViewModel::class.java)
        new_note_button_save.setOnClickListener {
            if (viewModel.insertNote(text_input_title.editText!!.text.toString(), text_input_description.editText!!.text.toString()))
                onBackPressed()
        }

        viewModel.observeTitleErrors().observe(this, { s ->
            text_input_title.error = s
            text_input_title.isErrorEnabled = s.isNotEmpty()
        })

        viewModel.observeDescriptionErrors().observe(this, { s ->
            text_input_description.error = s
            text_input_description.isErrorEnabled = s.isNotEmpty()
        })
    }

    override fun onSupportNavigateUp () : Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}
