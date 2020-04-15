package com.mancel.yann.realestatemanager.koin

import com.mancel.yann.realestatemanager.databases.AppDatabase
import com.mancel.yann.realestatemanager.repositories.*
import com.mancel.yann.realestatemanager.viewModels.RealEstateViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by Yann MANCEL on 17/03/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.koin
 */

val appModule = module {

    // Database
    single { AppDatabase.getDatabase(get()) }

    // DAO
    single { get<AppDatabase>().userDAO() }
    single { get<AppDatabase>().realEstateDAO() }
    single { get<AppDatabase>().photoDAO() }
    single { get<AppDatabase>().pointOfInterestDAO() }
    single { get<AppDatabase>().realEstatePointOfInterestCrossRefDAO() }

    // Repository
    single<PlaceRepository> { PlaceRepositoryImpl() }
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<RealEstateRepository> { RealEstateRepositoryImpl(get()) }
    single<PhotoRepository> { PhotoRepositoryImpl(get()) }
    single<PointOfInterestRepository> { PointOfInterestRepositoryImpl(get()) }
    single<RealEstatePointOfInterestCrossRefRepository> { RealEstatePointOfInterestCrossRefRepositoryImpl(get()) }

    // ViewModel
    viewModel { RealEstateViewModel(get(), get(), get(), get()) }
}