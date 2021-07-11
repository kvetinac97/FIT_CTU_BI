package cz.fit.cvut.wrzecond.feedreader.room

import androidx.room.TypeConverter
import java.util.*

class AppConverter {
    @TypeConverter
    fun fromTimestamp (value: Long?) = if (value == null) null else Date(value)
    @TypeConverter
    fun toTimestamp (date: Date?) = date?.time
}