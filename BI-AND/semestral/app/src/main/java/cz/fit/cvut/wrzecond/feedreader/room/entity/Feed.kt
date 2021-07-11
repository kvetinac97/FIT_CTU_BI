package cz.fit.cvut.wrzecond.feedreader.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import cz.fit.cvut.wrzecond.feedreader.room.dao.FeedDAO
import java.util.*

@Entity (tableName = FeedDAO.TABLE)
data class Feed (
    @PrimaryKey(autoGenerate = true) val id: Long?,
    val url: String,
    val title: String,
    val date: Date
) {
    constructor (url: String) : this(null, url, "", Date())
}
