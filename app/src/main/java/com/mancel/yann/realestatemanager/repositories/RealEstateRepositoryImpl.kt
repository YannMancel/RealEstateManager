package com.mancel.yann.realestatemanager.repositories

import androidx.lifecycle.LiveData
import com.mancel.yann.realestatemanager.dao.RealEstateDAO
import com.mancel.yann.realestatemanager.models.IdTypeAddressPriceTupleOfRealEstate
import com.mancel.yann.realestatemanager.models.RealEstate
import com.mancel.yann.realestatemanager.models.RealEstateWithPhotos
import com.mancel.yann.realestatemanager.models.RealEstateWithPointsOfInterest

/**
 * Created by Yann MANCEL on 05/03/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.repositories
 *
 * A class which implements [RealEstateRepository].
 */
class RealEstateRepositoryImpl(private val mRealEstateDAO: RealEstateDAO) : RealEstateRepository {

    // METHODS -------------------------------------------------------------------------------------

    // -- Create --

    override suspend fun insertRealEstate(realEstate: RealEstate): Long {
        return this.mRealEstateDAO.insertRealEstate(realEstate)
    }

    override suspend fun insertRealEstates(vararg realEstates: RealEstate): List<Long> {
        return this.mRealEstateDAO.insertRealEstates(*realEstates)
    }

    // -- Read --

    override fun getRealEstateById(realEstateId: Long): LiveData<RealEstate> {
        return this.mRealEstateDAO.getRealEstateById(realEstateId)
    }

    override fun getAllRealEstates(): LiveData<List<RealEstate>> {
        return this.mRealEstateDAO.getAllRealEstates()
    }

    override fun getCountOfRealEstatesByUserId(userId: Long): LiveData<Int> {
        return this.mRealEstateDAO.getCountOfRealEstatesByUserId(userId)
    }

    override fun getIdTypeAddressPriceTupleOfRealEstateByUserId(userId: Long): LiveData<List<IdTypeAddressPriceTupleOfRealEstate>> {
        return this.mRealEstateDAO.getIdTypeAddressPriceTupleOfRealEstateByUserId(userId)
    }

    override fun getRealEstatesWithPhotosByUserId(userId: Long): LiveData<List<RealEstateWithPhotos>> {
        return this.mRealEstateDAO.getRealEstatesWithPhotosByUserId(userId)
    }

    override fun getRealEstatesWithPointsOfInterestByUserId(userId: Long): LiveData<List<RealEstateWithPointsOfInterest>> {
        return this.mRealEstateDAO.getRealEstatesWithPointsOfInterestByUserId(userId)
    }

    // -- Update --

    override suspend fun updateRealEstate(realEstate: RealEstate): Int {
        return this.mRealEstateDAO.updateRealEstate(realEstate)
    }

    // -- Delete --

    override suspend fun deleteRealEstate(realEstate: RealEstate): Int {
        return this.mRealEstateDAO.deleteRealEstate(realEstate)
    }
}