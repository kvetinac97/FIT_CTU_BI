package cz.cvut.fit.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/** Note entity */
// 1 - doplnit Room anotace. Id je generovany primarni klic. Sloupce budeme chtit lower_case pojmenovanim.
@Entity
data class Note (
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val description: String,
    @ColumnInfo(name = "date_added") val dateAdded: Long
)
