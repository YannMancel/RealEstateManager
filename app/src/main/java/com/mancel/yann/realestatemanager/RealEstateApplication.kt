package com.mancel.yann.realestatemanager

import android.app.Application
import androidx.room.Room
import com.mancel.yann.realestatemanager.databases.AppDatabase

/**
 * Created by Yann MANCEL on 26/02/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager
 *
 * An [Application] subclass.
 */
class RealEstateApplication : Application() {

    // METHODS -------------------------------------------------------------------------------------

    companion object {
        lateinit var database: AppDatabase
    }

    // -- Application --

    override fun onCreate() {
        super.onCreate()

        // Singleton of the database
        database = Room.databaseBuilder(this.applicationContext,
                                        AppDatabase::class.java,
                                       "RealEstateManagerDatabase")
                       .build()
    }
}