package com.mancel.yann.realestatemanager.repositories

import com.mancel.yann.realestatemanager.api.GoogleMapsService
import com.mancel.yann.realestatemanager.models.PointOfInterest
import com.mancel.yann.realestatemanager.utils.MapperTools
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Created by Yann MANCEL on 15/04/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.repositories
 */
class GoogleMapsRepository : PlaceRepository {

    // FIELDS --------------------------------------------------------------------------------------

    private val mGoogleMapsService = GoogleMapsService.retrofit
                                                      .create(GoogleMapsService::class.java)

    // METHODS -------------------------------------------------------------------------------------

    override fun getStreamToFetchPointsOfInterest(
        location: String,
        radius: Double,
        types: String,
        key: String
    ): Single<List<PointOfInterest>> {
        return this.mGoogleMapsService
                   .getNearbySearch(location, radius, types, key)
                   .map { MapperTools.nearbySearchToPointsOfInterest(it) }
                   .subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread())
                   .timeout(10L, TimeUnit.SECONDS)
    }
}