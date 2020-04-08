package com.mancel.yann.realestatemanager.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mancel.yann.realestatemanager.models.IdTypeAddressPriceTupleOfRealEstate
import com.mancel.yann.realestatemanager.models.RealEstate
import com.mancel.yann.realestatemanager.models.RealEstateWithPhotos
import com.mancel.yann.realestatemanager.models.RealEstateWithPointsOfInterest

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
    suspend fun insertRealEstate(realEstate: RealEstate): Long

    /**
     * Usage:
     * val ids = dao.insertRealEstates(realEstate1, realEstate2)
     */
    @Insert
    suspend fun insertRealEstates(vararg realEstates: RealEstate): List<Long>

    // -- Read --

    /**
     * Usage:
     * dao.getRealEstateById(realEstateId)
     *    .observe(this, Observer { realEstate -> ... })
     */
    @Query("""
        SELECT * 
        FROM real_estate 
        WHERE id_real_estate = :realEstateId
        """)
    fun getRealEstateById(realEstateId: Long): LiveData<RealEstate>

    /**
     * Usage:
     * dao.getAllRealEstates()
     *    .observe(this, Observer { realEstates -> ... })
     */
    @Query("""
        SELECT * 
        FROM real_estate
        """)
    fun getAllRealEstates(): LiveData<List<RealEstate>>

    /**
     * Usage:
     * dao.getCountOfRealEstatesByUserId(userId)
     *    .observe(this, Observer { count -> ... })
     */
    @Query("""
        SELECT count(*) 
        FROM real_estate 
        WHERE estate_agent_id = :userId
        """)
    fun getCountOfRealEstatesByUserId(userId: Long): LiveData<Int>

    /**
     * Usage:
     * dao.getIdTypeAddressPriceTupleOfRealEstateByUserId(userId)
     *    .observe(this, Observer { tuples -> ... })
     */
    @Query("""
        SELECT id_real_estate, 
               type, 
               price_dollar, 
               loc_street, loc_city, loc_post_code, loc_country, loc_latitude, loc_longitude
        FROM real_estate
        WHERE estate_agent_id = :userId
        """)
    fun getIdTypeAddressPriceTupleOfRealEstateByUserId(
        userId: Long
    ): LiveData<List<IdTypeAddressPriceTupleOfRealEstate>>

    /**
     * Usage:
     * dao.getRealEstatesWithPhotosByUserId(userId)
     *    .observe(this, Observer { realEstatesWithPhotos -> ... })
     */
    @Transaction
    @Query("""
        SELECT * 
        FROM real_estate 
        WHERE estate_agent_id = :userId
        """)
    fun getRealEstatesWithPhotosByUserId(
        userId: Long
    ): LiveData<List<RealEstateWithPhotos>>

    /**
     * Usage:
     * dao.getRealEstatesWithPointsOfInterestByUserId(userId)
     *    .observe(this, Observer { realEstatesWithPointsOfInterest -> ... })
     */
    @Transaction
    @Query("""
        SELECT * 
        FROM real_estate 
        WHERE estate_agent_id = :userId
        """)
    fun getRealEstatesWithPointsOfInterestByUserId(
        userId: Long
    ): LiveData<List<RealEstateWithPointsOfInterest>>

    /**
     * Usage:
     * dao.getRealEstateWithPhotosById(realEstateId)
     *    .observe(this, Observer { RealEstateWithPhotos -> ... })
     */
    @Transaction
    @Query("""
        SELECT * 
        FROM real_estate 
        WHERE id_real_estate = :realEstateId
        """)
    fun getRealEstateWithPhotosById(
        realEstateId: Long
    ): LiveData<RealEstateWithPhotos>

    // -- Update --

    /**
     * Usage:
     * val numberOfUpdatedRow = dao.updateRealEstate(realEstate)
     */
    @Update
    suspend fun updateRealEstate(realEstate: RealEstate): Int

    // -- Delete --

    /**
     * Usage:
     * val numberOfDeletedRow = dao.deleteRealEstate(realEstate)
     */
    @Delete
    suspend fun deleteRealEstate(realEstate: RealEstate): Int
}