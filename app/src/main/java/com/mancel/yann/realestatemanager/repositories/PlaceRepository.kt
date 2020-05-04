package com.mancel.yann.realestatemanager.repositories

import com.mancel.yann.realestatemanager.models.PointOfInterest
import io.reactivex.Single

/**
 * Created by Yann MANCEL on 15/04/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.repositories
 */
interface PlaceRepository {

    // METHODS -------------------------------------------------------------------------------------

    /**
     * Gets stream to Fetch the [List] of [PointOfInterest]
     * @param location  a [String] that contains the latitude/longitude around which to retrieve place information
     * @param radius    a [Double] that defines the distance (in meters) within which to return place results
     * @param types     a [String] that restricts the results to places matching the specified type
     * @param key       a [String] that contains your application's API key
     * @return a [Single] of [List] of [PointOfInterest]
     */
    fun getStreamToFetchPointsOfInterest(
        location: String,
        radius: Double,
        types: String,
        key: String
    ): Single<List<PointOfInterest>>
}