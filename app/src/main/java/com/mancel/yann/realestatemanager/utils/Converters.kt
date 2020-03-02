package com.mancel.yann.realestatemanager.utils

import androidx.room.TypeConverter
import java.util.*

/**
 * Created by Yann MANCEL on 01/03/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.utils
 */
class Converters {

    /**
     * Converts an [Long] to a [Date]
     * @param value a [Long]
     * @return a [Date]
     */
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    /**
     * Converts a [Date] to an [Long]
     * @param date a [Date]
     * @return a [Long]
     */
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time
}