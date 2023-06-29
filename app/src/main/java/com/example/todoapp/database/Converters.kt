package com.example.todoapp.database

import androidx.room.TypeConverter
import java.util.Date
import java.util.UUID

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromString(value: String?): UUID? {
        return if (value != null) UUID.fromString(value) else null
    }

    @TypeConverter
    fun toString(uuid: UUID?): String? {
        return uuid?.toString()
    }
}