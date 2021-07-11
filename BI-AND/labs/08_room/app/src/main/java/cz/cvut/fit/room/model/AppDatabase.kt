package cz.cvut.fit.room.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/** Application database implemented with Room */
// 3 doplnit anotace k databazi
@Database(entities = [Note::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun notesDao(): NotesDao

    companion object {
        private var instance: AppDatabase? = null
        private const val DATABASE_NAME = "database-name"

        fun getInstance(context: Context): AppDatabase {
            // 4 vytvořit instanci AppDatabase z RoomDatabase
            if (instance == null)
                instance = Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()
            return instance!!
        }
    }
}
