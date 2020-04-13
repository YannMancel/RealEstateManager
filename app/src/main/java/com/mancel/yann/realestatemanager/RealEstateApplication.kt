package com.mancel.yann.realestatemanager

import androidx.multidex.MultiDexApplication
import com.mancel.yann.realestatemanager.koin.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

/**
 * Created by Yann MANCEL on 26/02/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager
 *
 * A [MultiDexApplication] subclass.
 */
class RealEstateApplication : MultiDexApplication() {

    // METHODS -------------------------------------------------------------------------------------

    // -- MultiDexApplication --

    override fun onCreate() {
        super.onCreate()

        // Timber: Logger
        Timber.plant(Timber.DebugTree())

        // Koin: Dependency injection framework
        startKoin {
            androidLogger()
            androidContext(this@RealEstateApplication)
            modules(appModule)
        }
    }
}