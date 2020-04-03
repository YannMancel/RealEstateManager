package com.mancel.yann.realestatemanager.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mancel.yann.realestatemanager.models.PointOfInterest

/**
 * Created by Yann MANCEL on 02/03/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.dao
 *
 * DAO of [PointOfInterest].
 */
@Dao
interface PointOfInterestDAO {

    // METHODS -------------------------------------------------------------------------------------

    // -- Create --

    /**
     * Usage:
     * val id = dao.insertPointOfInterest(pointOfInterest)
     */
    @Insert
    suspend fun insertPointOfInterest(pointOfInterest: PointOfInterest): Long

    /**
     * Usage:
     * val ids = dao.insertPointsOfInterest(pointOfInterest1, pointOfInterest2)
     */
    @Insert
    suspend fun insertPointsOfInterest(vararg pointsOfInterest: PointOfInterest): List<Long>

    // -- Read --

    /**
     * Usage:
     * dao.getAllPointsOfInterest()
     *    .observe(this, Observer { pointsOfInterest -> ... })
     */
    @Query("""
        SELECT * 
        FROM point_of_interest
        """)
    fun getAllPointsOfInterest(): LiveData<List<PointOfInterest>>
}