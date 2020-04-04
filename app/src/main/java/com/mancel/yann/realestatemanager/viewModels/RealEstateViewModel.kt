package com.mancel.yann.realestatemanager.viewModels

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mancel.yann.realestatemanager.liveDatas.PhotoCreatorLiveData
import com.mancel.yann.realestatemanager.models.*
import com.mancel.yann.realestatemanager.repositories.PhotoRepository
import com.mancel.yann.realestatemanager.repositories.RealEstateRepository
import com.mancel.yann.realestatemanager.repositories.UserRepository
import kotlinx.coroutines.*

/**
 * Created by Yann MANCEL on 09/03/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.viewModels
 *
 * A [ViewModel] subclass.
 */
class RealEstateViewModel(
    private val mUserRepository: UserRepository,
    private val mRealEstateRepository: RealEstateRepository,
    private val mPhotoRepository: PhotoRepository
) : ViewModel() {

    // FIELDS --------------------------------------------------------------------------------------

    private var mUser: LiveData<User>? = null

    private var mCountOfRealEstateByUserId: LiveData<Int>? = null
    private var mRealEstatesSimpleFormat: LiveData<List<IdTypeAddressPriceTupleOfRealEstate>>? = null

    private var mPhotosByRealEstateId: LiveData<List<Photo>>? = null
    private var mPhotos: LiveData<List<Photo>>? = null
    private var mPhotoCreator: PhotoCreatorLiveData? = null

    // CONSTRUCTORS --------------------------------------------------------------------------------

    init {
        Log.d(this::class.java.simpleName, "RealEstateViewModel: INIT")
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- ViewModel --

    override fun onCleared() {
        super.onCleared()
        Log.d(this::class.java.simpleName, "RealEstateViewModel: onCleared")
    }

    // -- User --

    /**
     * Inserts the new [User] in argument
     * @param user a [User]
     */
    fun insertUser(user: User) = viewModelScope.launch(Dispatchers.IO) {
        // Fetch the new rowId for the inserted item
        val deferred = async { this@RealEstateViewModel.mUserRepository.insertUser(user) }

        try {
            Log.i(this@RealEstateViewModel::class.java.simpleName, "insertUser: id=${deferred.await()}")
        }
        catch (e: SQLiteConstraintException) {
            // UNIQUE constraint failed
            Log.e(this@RealEstateViewModel::class.java.simpleName, "insertUser: ${e.message}")
        }
    }

    /**
     * Gets the [User] with the id in argument
     * @param userId a [Long] that contains the user Id
     * @return a [LiveData] of [User]
     */
    fun getUserById(userId: Long): LiveData<User> {
        if (this.mUser == null) {
            this.mUser = this.mUserRepository.getUserById(userId)
        }
        return this.mUser!!
    }

    // -- Real Estate --

    /**
     * Inserts the new [RealEstate] in argument
     * @param realEstate        a [RealEstate]
     * @param photos            a [List] of [Photo]
     * @param pointsOfInterest  a [List] of [PointOfInterest]
     */
    fun insertRealEstate(
        realEstate: RealEstate,
        photos: List<Photo>? = null,
        pointsOfInterest: List<PointOfInterest>? = null
    ) = viewModelScope.launch(Dispatchers.IO) {
        // Fetch the new rowId for the inserted item
        val deferred = async { this@RealEstateViewModel.mRealEstateRepository.insertRealEstate(realEstate) }

        try {
            val realEstateId = deferred.await()
            Log.w(this@RealEstateViewModel::class.java.simpleName, "insertRealEstate: id=${realEstateId}")
        }
        catch (e: SQLiteConstraintException) {
            // UNIQUE constraint failed
            Log.e(this@RealEstateViewModel::class.java.simpleName, "insertRealEstate: ${e.message}")
        }
    }

    /**
     * Gets the count of row where user Id is validated
     * @param userId a [Long] that contains the user Id
     * @return a [LiveData] of [Int]
     */
    fun getCountOfRealEstatesByUserId(userId: Long): LiveData<Int> {
        if (this.mCountOfRealEstateByUserId == null) {
            this.mCountOfRealEstateByUserId = this.mRealEstateRepository.getCountOfRealEstatesByUserId(userId)
        }
        return this.mCountOfRealEstateByUserId!!
    }

    /**
     * Gets all [IdTypeAddressPriceTupleOfRealEstate] thanks to [LiveData]
     * @param userId a [Long] that contains the user Id
     * @return a [LiveData] of [IdTypeAddressPriceTupleOfRealEstate]
     */
    fun getRealEstatesSimpleFormatByUserId(userId: Long): LiveData<List<IdTypeAddressPriceTupleOfRealEstate>> {
        if (this.mRealEstatesSimpleFormat == null) {
            this.mRealEstatesSimpleFormat = this.mRealEstateRepository.getIdTypeAddressPriceTupleOfRealEstateByUserId(userId)
        }
        return this.mRealEstatesSimpleFormat!!
    }

    // -- Photo --

    /**
     * Gets all [Photo] with the same real estate id
     * @param realEstateId a [Long] that corresponds to the real estate id
     * @return a [LiveData] of [List] of [Photo]
     */
    fun getPhotosByRealEstateId(realEstateId: Long): LiveData<List<Photo>> {
        if (this.mPhotosByRealEstateId == null) {
            this.mPhotosByRealEstateId = this.mPhotoRepository.getPhotoByRealEstateId(realEstateId)
        }
        return this.mPhotosByRealEstateId!!
    }

    /**
     * Gets all [Photo]
     * @return a [LiveData] of [List] of [Photo]
     */
    fun getPhotos(): LiveData<List<Photo>> {
        if (this.mPhotos == null) {
            this.mPhotos = this.mPhotoRepository.getAllPhotos()
        }
        return this.mPhotos!!
    }

    /**
     * Gets a [PhotoCreatorLiveData]
     * @return a [PhotoCreatorLiveData]
     */
    fun getPhotoCreator(): PhotoCreatorLiveData {
        if (this.mPhotoCreator == null) {
            this.mPhotoCreator = PhotoCreatorLiveData()
        }
        return this.mPhotoCreator!!
    }
}