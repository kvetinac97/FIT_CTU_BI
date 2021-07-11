package cz.cvut.fit.room.ui.notes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import cz.cvut.fit.room.model.AppDatabase
import cz.cvut.fit.room.model.Note
import cz.cvut.fit.room.model.NotesRepository

/** ViewModel of notes list screen */
class NotesViewModel(application: Application) : AndroidViewModel(application) {
    private val notesRepository = NotesRepository (AppDatabase.getInstance(application).notesDao())
    fun observeNotes() = notesRepository.observeNotes()
    // 8 smazat note v repository
    fun deleteNote(note: Note) = notesRepository.deleteNote(note)
}
