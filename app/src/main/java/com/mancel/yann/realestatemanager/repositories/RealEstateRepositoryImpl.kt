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
class RealEstateRepositoryImpl(private val mRealEstateDAO: RealEstateDAO): RealEstateRepository {

    // METHODS -------------------------------------------------------------------------------------

    // -- Create --

    override fun insertRealEstate(realEstate: RealEstate): Long {
        TODO("Not yet implemented")
    }

    override fun insertRealEstates(vararg realEstates: RealEstate): List<Long> {
        TODO("Not yet implemented")
    }

    // -- Read --

    override fun getRealEstateById(realEstateId: Long): LiveData<RealEstate> {
        TODO("Not yet implemented")
    }

    override fun getAllRealEstates(): LiveData<List<RealEstate>> {
        TODO("Not yet implemented")
    }

    override fun getIdTypeAddressPriceTupleOfRealEstate(): LiveData<List<IdTypeAddressPriceTupleOfRealEstate>> {
        TODO("Not yet implemented")
    }

    override fun getRealEstatesWithPhotos(): LiveData<List<RealEstateWithPhotos>> {
        TODO("Not yet implemented")
    }

    override fun getRealEstatesWithPointsOfInterest(): LiveData<List<RealEstateWithPointsOfInterest>> {
        TODO("Not yet implemented")
    }

    // -- Update --

    override fun updateRealEstate(realEstate: RealEstate): Int {
        TODO("Not yet implemented")
    }

    // -- Delete --

    override fun deleteRealEstate(realEstate: RealEstate): Int {
        TODO("Not yet implemented")
    }
}