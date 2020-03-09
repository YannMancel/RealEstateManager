package com.mancel.yann.realestatemanager.repositories

import androidx.lifecycle.LiveData
import com.mancel.yann.realestatemanager.models.IdTypeAddressPriceTupleOfRealEstate
import com.mancel.yann.realestatemanager.models.RealEstate
import com.mancel.yann.realestatemanager.models.RealEstateWithPhotos
import com.mancel.yann.realestatemanager.models.RealEstateWithPointsOfInterest

/**
 * Created by Yann MANCEL on 06/03/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.repositories
 */
interface RealEstateRepository {

    // METHODS -------------------------------------------------------------------------------------

    // -- Create --

    fun insertRealEstate(realEstate: RealEstate): Long

    fun insertRealEstates(vararg realEstates: RealEstate): List<Long>

    // -- Read --

    fun getRealEstateById(realEstateId: Long): LiveData<RealEstate>

    fun getAllRealEstates(): LiveData<List<RealEstate>>

    fun getIdTypeAddressPriceTupleOfRealEstate(): LiveData<List<IdTypeAddressPriceTupleOfRealEstate>>

    fun getRealEstatesWithPhotos(): LiveData<List<RealEstateWithPhotos>>

    fun getRealEstatesWithPointsOfInterest(): LiveData<List<RealEstateWithPointsOfInterest>>

    // -- Update --

    fun updateRealEstate(realEstate: RealEstate): Int

    // -- Delete --

    fun deleteRealEstate(realEstate: RealEstate): Int
}