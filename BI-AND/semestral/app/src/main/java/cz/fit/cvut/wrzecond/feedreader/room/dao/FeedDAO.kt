package cz.fit.cvut.wrzecond.feedreader.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import cz.fit.cvut.wrzecond.feedreader.room.entity.Feed

@Dao
interface FeedDAO {

    @Query("SELECT * FROM $TABLE")
    fun findAll () : LiveData<List<Feed>>

    @Query("SELECT id FROM $TABLE WHERE url = :url")
    fun findByUrl (url: String) : Long?

    @Insert
    suspend fun insert (feed: Feed) : Long

    @Update
    suspend fun update (feed: Feed)

    @Query ("DELETE FROM $TABLE WHERE id = :id")
    suspend fun delete (id: Long)

    companion object {
        const val TABLE = "feed"
    }

}
