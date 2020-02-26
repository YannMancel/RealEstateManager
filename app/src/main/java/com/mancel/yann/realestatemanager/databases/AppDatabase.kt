package com.mancel.yann.realestatemanager.databases

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mancel.yann.realestatemanager.dao.UserDAO
import com.mancel.yann.realestatemanager.models.User

/**
 * Created by Yann MANCEL on 26/02/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.databases
 *
 * A [RoomDatabase] subclass.
 */
@Database(entities = arrayOf(User::class),
          version = 1)
abstract class AppDatabase : RoomDatabase() {

    // DAOs ----------------------------------------------------------------------------------------

    abstract fun userDAO(): UserDAO
}