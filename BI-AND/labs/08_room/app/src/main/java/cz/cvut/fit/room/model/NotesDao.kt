package cz.cvut.fit.room.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

/** Room DAO for Note entity */
// 2 doplnit anotace k metodam DAO. Poznamky budeme chtit radit od nejnovejsi.
@Dao
interface NotesDao {
    @Insert fun insertNote (note: Note)
    @Delete fun deleteNote (note: Note)

    @Query("SELECT * FROM $TABLE_NOTES ORDER BY date_added")
    fun observeNotes () : LiveData<List<Note>>

    companion object {
        const val TABLE_NOTES = "note"
    }
}
