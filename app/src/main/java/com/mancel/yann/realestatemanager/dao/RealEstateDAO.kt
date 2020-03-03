package com.mancel.yann.realestatemanager.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mancel.yann.realestatemanager.models.RealEstate

/**
 * Created by Yann MANCEL on 03/03/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.dao
 *
 * DAO of [RealEstate].
 */
@Dao
interface RealEstateDAO {

    // METHODS -------------------------------------------------------------------------------------

    // -- Create --

    /**
     * Usage:
     * val id = dao.insertRealEstate(realEstate)
     */
    @Insert
    fun insertRealEstate(realEstate: RealEstate): Long

    /**
     * Usage:
     * val ids = dao.insertRealEstates(realEstate1, realEstate2)
     */
    @Insert
    fun insertRealEstates(vararg realEstates: RealEstate): List<Long>
}