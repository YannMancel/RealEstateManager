package com.mancel.yann.realestatemanager

import android.app.Application
import com.mancel.yann.realestatemanager.koin.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * Created by Yann MANCEL on 26/02/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager
 *
 * An [Application] subclass.
 */
class RealEstateApplication : Application() {

    // METHODS -------------------------------------------------------------------------------------

    // -- Application --

    override fun onCreate() {
        super.onCreate()

        // KOIN: Dependency injection framework
        startKoin {
            androidLogger()
            androidContext(this@RealEstateApplication)
            modules(appModule)
        }
    }
}