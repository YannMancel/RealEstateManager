package com.mancel.yann.realestatemanager.utils

import com.mancel.yann.realestatemanager.models.Address
import com.mancel.yann.realestatemanager.models.NearbySearch
import com.mancel.yann.realestatemanager.models.PointOfInterest

/**
 * Created by Yann MANCEL on 15/04/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.utils
 */
object MapperTools {

    /**
     * A mapper to convert [NearbySearch] to [List] of [PointOfInterest]
     * @param nearbySearch a [NearbySearch]
     * @return a [List] of [PointOfInterest]
     */
    fun nearbySearchToPointsOfInterest(nearbySearch: NearbySearch): List<PointOfInterest> {
        return mutableListOf<PointOfInterest>().apply {
            nearbySearch.results?.forEach {
                this.add(
                    PointOfInterest(
                        mName = it.name ?: "Point of interest",
                        mAddress = Address(
                            mLatitude = it.geometry?.location?.lat ?: 0.0,
                            mLongitude = it.geometry?.location?.lng ?: 0.0
                        )
                    )
                )
            }
        }
    }
}