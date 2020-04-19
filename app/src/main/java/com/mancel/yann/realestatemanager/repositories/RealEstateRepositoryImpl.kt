package com.mancel.yann.realestatemanager.repositories

import androidx.lifecycle.LiveData
import com.mancel.yann.realestatemanager.dao.RealEstateDAO
import com.mancel.yann.realestatemanager.models.RealEstate
import com.mancel.yann.realestatemanager.models.RealEstateWithPhotos
import com.mancel.yann.realestatemanager.models.RealEstateWithPointsOfInterest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by Yann MANCEL on 05/03/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.repositories
 *
 * A class which implements [RealEstateRepository].
 */
class RealEstateRepositoryImpl(
    private val mRealEstateDAO: RealEstateDAO
) : RealEstateRepository {

    // METHODS -------------------------------------------------------------------------------------

    // -- Create --

    override suspend fun insertRealEstate(
        realEstate: RealEstate
    ): Long = withContext(Dispatchers.IO) {
        this@RealEstateRepositoryImpl.mRealEstateDAO.insertRealEstate(realEstate)
    }

    // -- Read --

    override fun getRealEstatesWithPhotosByUserId(
        userId: Long
    ): LiveData<List<RealEstateWithPhotos>> {
        return this.mRealEstateDAO.getRealEstatesWithPhotosByUserId(userId)
    }

    override fun getRealEstateWithPhotosById(realEstateId: Long): LiveData<RealEstateWithPhotos> {
        return this.mRealEstateDAO.getRealEstateWithPhotosById(realEstateId)
    }

    override fun getRealEstateWithPointsOfInterestById(
        realEstateId: Long
    ): LiveData<RealEstateWithPointsOfInterest> {
        return this.mRealEstateDAO.getRealEstateWithPointsOfInterestById(realEstateId)
    }

    // -- Update --

    override suspend fun updateRealEstate(
        realEstate: RealEstate
    ): Int = withContext(Dispatchers.IO) {
        this@RealEstateRepositoryImpl.mRealEstateDAO.updateRealEstate(realEstate)
    }
}