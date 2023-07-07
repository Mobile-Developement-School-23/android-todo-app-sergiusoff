package com.example.todoapp.database

import androidx.room.TypeConverter
import java.util.Date
import java.util.UUID

/**
 * Класс-конвертер для преобразования типов при работе с базой данных Room.
 */
class Converters {
    /**
     * Преобразует значение временной метки в объект типа `Date`.
     *
     * @param value Значение временной метки.
     * @return Объект типа `Date` или `null`, если значение равно `null`.
     */
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    /**
     * Преобразует объект типа `Date` в значение временной метки.
     *
     * @param date Объект типа `Date`.
     * @return Значение временной метки или `null`, если объект равен `null`.
     */
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    /**
     * Преобразует строку в объект типа `UUID`.
     *
     * @param value Строка, представляющая UUID.
     * @return Объект типа `UUID` или `null`, если строка равна `null`.
     */
    @TypeConverter
    fun fromString(value: String?): UUID? {
        return if (value != null) UUID.fromString(value) else null
    }

    /**
     * Преобразует объект типа `UUID` в строку.
     *
     * @param uuid Объект типа `UUID`.
     * @return Строковое представление UUID или `null`, если объект равен `null`.
     */
    @TypeConverter
    fun toString(uuid: UUID?): String? {
        return uuid?.toString()
    }
}