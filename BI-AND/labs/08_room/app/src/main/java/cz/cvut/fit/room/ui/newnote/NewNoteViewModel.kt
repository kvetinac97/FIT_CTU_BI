package cz.cvut.fit.room.ui.newnote

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import cz.cvut.fit.room.R
import cz.cvut.fit.room.model.AppDatabase
import cz.cvut.fit.room.model.NotesRepository

/**
 * ViewModel of a screen with new note form
 */
class NewNoteViewModel(application: Application) : AndroidViewModel(application) {
    // 9 iniicializovat repozitory, viz NotesViewModel
    private var notesRepository = NotesRepository(AppDatabase.getInstance(application).notesDao())
    private val titleError = MutableLiveData<String>()
    private val descriptionError = MutableLiveData<String>()

    fun insertNote(title: String, description: String): Boolean {
        var valid = true
        if (title.isEmpty()) {
            titleError.postValue(getApplication<Application>().getString(R.string.new_note_error_title_empty))
            valid = false
        }
        else
            titleError.postValue("")
        if (description.isEmpty()) {
            descriptionError.postValue(getApplication<Application>().getString(R.string.new_note_error_description_empty))
            valid = false
        }
        else
            descriptionError.postValue("")
        // 10 vložit poznámku skrz repository
        if (valid)
            notesRepository.insertNote(title, description)
        return valid
    }

    fun observeTitleErrors() = titleError
    fun observeDescriptionErrors() = descriptionError
}
