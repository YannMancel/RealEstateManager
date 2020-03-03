package com.mancel.yann.realestatemanager.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mancel.yann.realestatemanager.models.IdTypeAddressPriceTupleOfRealEstate
import com.mancel.yann.realestatemanager.models.RealEstate
import com.mancel.yann.realestatemanager.models.RealEstateWithPhotos

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

    // -- Read --

    /**
     * Usage:
     * dao.getRealEstateById(realEstateId)
     *    .observe(this, Observer { realEstate -> ... })
     */
    @Query("SELECT * FROM real_estate WHERE id = :realEstateId")
    fun getRealEstateById(realEstateId: Long): LiveData<RealEstate>

    /**
     * Usage:
     * dao.getAllRealEstates()
     *    .observe(this, Observer { realEstates -> ... })
     */
    @Query("SELECT * FROM real_estate")
    fun getAllRealEstates(): LiveData<List<RealEstate>>

    /**
     * Usage:
     * dao.getIdTypeAddressPriceTupleOfRealEstate()
     *    .observe(this, Observer { tuples -> ... })
     */
    @Query("""SELECT id, 
                           type, 
                           price_dollar, 
                           loc_street, loc_state, loc_city, loc_post_code
                    FROM real_estate""")
    fun getIdTypeAddressPriceTupleOfRealEstate(): LiveData<List<IdTypeAddressPriceTupleOfRealEstate>>

    /**
     * Usage:
     * dao.getRealEstatesWithPhotos()
     *    .observe(this, Observer { realEstatesWithPhotos -> ... })
     */
    @Transaction
    @Query("SELECT * FROM real_estate")
    fun getRealEstatesWithPhotos(): LiveData<List<RealEstateWithPhotos>>

    // -- Update --

    /**
     * Usage:
     * val numberOfUpdatedRow = dao.updateRealEstate(realEstate)
     */
    @Update
    fun updateRealEstate(realEstate: RealEstate): Int

    // -- Delete --

    /**
     * Usage:
     * val numberOfDeletedRow = dao.deleteRealEstate(realEstate)
     */
    @Delete
    fun deleteRealEstate(realEstate: RealEstate): Int
}