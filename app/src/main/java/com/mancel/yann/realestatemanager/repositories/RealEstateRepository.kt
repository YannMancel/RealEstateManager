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

    suspend fun insertRealEstate(realEstate: RealEstate): Long

    suspend fun insertRealEstates(vararg realEstates: RealEstate): List<Long>

    // -- Read --

    fun getRealEstateById(realEstateId: Long): LiveData<RealEstate>

    fun getAllRealEstates(): LiveData<List<RealEstate>>

    fun getCountOfRealEstatesByUserId(userId: Long): LiveData<Int>

    fun getIdTypeAddressPriceTupleOfRealEstateByUserId(userId: Long): LiveData<List<IdTypeAddressPriceTupleOfRealEstate>>

    fun getRealEstatesWithPhotosByUserId(userId: Long): LiveData<List<RealEstateWithPhotos>>

    fun getRealEstatesWithPointsOfInterestByUserId(userId: Long): LiveData<List<RealEstateWithPointsOfInterest>>

    fun getRealEstateWithPhotosById(realEstateId: Long): LiveData<RealEstateWithPhotos>

    // -- Update --

    suspend fun updateRealEstate(realEstate: RealEstate): Int

    // -- Delete --

    suspend fun deleteRealEstate(realEstate: RealEstate): Int
}