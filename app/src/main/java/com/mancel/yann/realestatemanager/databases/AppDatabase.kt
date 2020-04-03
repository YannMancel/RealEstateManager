package com.mancel.yann.realestatemanager.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mancel.yann.realestatemanager.dao.*
import com.mancel.yann.realestatemanager.models.*
import com.mancel.yann.realestatemanager.utils.Converters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by Yann MANCEL on 26/02/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.databases
 *
 * A [RoomDatabase] subclass.
 */
@Database(entities = [User::class,
                      RealEstate::class,
                      Photo::class,
                      PointOfInterest::class,
                      RealEstatePointOfInterestCrossRef::class],
          version = 1,
          exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    // See: https://codelabs.developers.google.com/codelabs/android-room-with-a-view-kotlin

    // DAOs ----------------------------------------------------------------------------------------

    abstract fun userDAO(): UserDAO
    abstract fun realEstateDAO(): RealEstateDAO
    abstract fun photoDAO(): PhotoDAO
    abstract fun pointOfInterestDAO(): PointOfInterestDAO
    abstract fun realEstatePointOfInterestCrossRefDAO(): RealEstatePointOfInterestCrossRefDAO

    // METHODS -------------------------------------------------------------------------------------

    companion object {

        private const val DATABASE_NAME = "RealEstateManager_Database"

        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Gets the [AppDatabase]
         * @param context a [Context]
         * @return the [AppDatabase]
         */
        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE

            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,
                                                    AppDatabase::class.java,
                                                    DATABASE_NAME)
                                   .addCallback(UserDatabaseCallback())
                                   .build()

                INSTANCE = instance

                return instance
            }
        }
    }

    // PRIVATE CLASSES -----------------------------------------------------------------------------

    /**
     * A [RoomDatabase.Callback] subclass.
     */
    private class UserDatabaseCallback : RoomDatabase.Callback() {

        // METHODS ---------------------------------------------------------------------------------

        // -- RoomDatabase.Callback --

        // If you only want to populate the database the first time the app is launched.
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    this@UserDatabaseCallback.populateDatabase(database.userDAO())
                }
            }
        }

        // -- User --

        /**
         * Populates the [AppDatabase] with an [User]
         * @param userDAO a DAO for the [User] table
         */
        private suspend fun populateDatabase(userDAO: UserDAO) {
            // Add a User to the database
            val user = User(mUsername = "User",
                            mEmail = "user@gmail.com",
                            mUrlPicture = "")

            userDAO.insertUser(user)
        }
    }
}