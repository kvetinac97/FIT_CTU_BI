package cz.cvut.fit.room

import android.app.Application
import cz.cvut.fit.room.model.AppDatabase
import cz.cvut.fit.room.model.NotesRepository

/**
 * App's application class
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        // 7 za pomoci SharedPreferences zařiďte, aby se data vkládaly pouze jednou po prvním spuštění
        val preferences = getSharedPreferences(REPOSITORY_PREFERENCES, MODE_PRIVATE)
        if ( preferences.getBoolean(REPOSITORY_LOADED, false) )
            return
        preferences.edit().putBoolean(REPOSITORY_LOADED, true).apply()

        // 5 odkomentujte kod. Při každém spuštění aplikace se nám vloží data do databáze.
        val dao = AppDatabase.getInstance(this).notesDao()
        val notesRepository = NotesRepository(dao)

        notesRepository.insertNote("First note", "First description")
        notesRepository.insertNote("Second note", "Second description")
        notesRepository.insertNote("Third note", "Third description")
    }

    companion object {
        const val REPOSITORY_PREFERENCES = "REPOSITORY_PREFERENCES"
        const val REPOSITORY_LOADED = "REPOSITORY_LOADED"
    }

}
