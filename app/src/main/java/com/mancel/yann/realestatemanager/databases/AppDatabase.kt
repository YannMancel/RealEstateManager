package com.mancel.yann.realestatemanager.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mancel.yann.realestatemanager.dao.UserDAO
import com.mancel.yann.realestatemanager.models.User
import com.mancel.yann.realestatemanager.utils.Converters

/**
 * Created by Yann MANCEL on 26/02/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.databases
 *
 * A [RoomDatabase] subclass.
 */
@Database(entities = arrayOf(User::class),
          version = 1,
          exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    // See: https://codelabs.developers.google.com/codelabs/android-room-with-a-view-kotlin/#6

    // DAOs ----------------------------------------------------------------------------------------

    abstract fun userDAO(): UserDAO

    // METHODS -------------------------------------------------------------------------------------

    companion object {

        private const val DATABASE_NAME = "RealEstateManager_Database"

        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE

            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,
                                                    AppDatabase::class.java,
                                                    DATABASE_NAME)
                                   .build()

                INSTANCE = instance

                return instance
            }
        }
    }
}