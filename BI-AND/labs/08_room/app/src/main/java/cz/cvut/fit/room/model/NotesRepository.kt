package cz.cvut.fit.room.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/** Repository for accessing notes */
class NotesRepository (private val notesDao: NotesDao) {

    fun insertNote (title: String, description: String)
        = doAsync { notesDao.insertNote(Note(0, title, description, System.currentTimeMillis())) }

    fun deleteNote   (note: Note) = doAsync { notesDao.deleteNote(note) }
    fun observeNotes () = notesDao.observeNotes()

    private fun doAsync (run: () -> Unit)
        = GlobalScope.launch { withContext(Dispatchers.IO) { run() } }

}
