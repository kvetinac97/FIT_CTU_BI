package cz.fit.cvut.wrzecond.feedreader.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import cz.fit.cvut.wrzecond.feedreader.room.entity.Article

@Dao
interface ArticleDAO {

    @Query("SELECT * FROM $TABLE")
    fun findAll () : LiveData<List<Article>>

    @Query("SELECT id FROM $TABLE WHERE link = :link")
    fun getByLink (link: String) : Long?

    @Query("SELECT * FROM $TABLE WHERE id = :id")
    fun getByID (id: Long) : LiveData<Article>

    @Insert
    suspend fun insert (article: Article)

    @Update
    suspend fun update (article: Article)

    companion object {
        const val TABLE = "articles"
    }

}
