package com.mancel.yann.realestatemanager.viewModels

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mancel.yann.realestatemanager.liveDatas.LocationLiveData
import com.mancel.yann.realestatemanager.liveDatas.PhotoCreatorLiveData
import com.mancel.yann.realestatemanager.models.*
import com.mancel.yann.realestatemanager.repositories.PhotoRepository
import com.mancel.yann.realestatemanager.repositories.RealEstateRepository
import com.mancel.yann.realestatemanager.repositories.UserRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

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

    private var mLocation: LocationLiveData? = null

    private var mUser: LiveData<User>? = null

    private var mCountOfRealEstateByUserId: LiveData<Int>? = null
    private var mRealEstatesWithPhotos: LiveData<List<RealEstateWithPhotos>>? = null
    private var mRealEstateWithPhotos: LiveData<RealEstateWithPhotos>? = null

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

    // -- Location --

    /**
     * Gets the [LiveData] of [LocationData]
     * @param context a [Context]
     * @return a [LiveData] of [LocationData]
     */
    fun getLocation(context: Context): LiveData<LocationData> {
        if (this.mLocation == null) {
            this.mLocation = LocationLiveData(context)
        }
        return this.mLocation!!
    }

    /**
     * Starts the location update from [LocationLiveData]
     */
    fun startLocationUpdate() = this.mLocation?.requestUpdateLocation()!!

    // -- User --

    /**
     * Inserts the new [User] in argument
     * @param user a [User]
     */
    fun insertUser(user: User) = viewModelScope.launch(Dispatchers.IO) {
        try {
            // Fetch the new rowId for the inserted item
            val userId: Long = this@RealEstateViewModel.mUserRepository.insertUser(user)
            Log.i(this@RealEstateViewModel::class.java.simpleName, "insertUser: id=$userId")
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
        val realEstateId: Long

        // REAL ESTATE
        try {
            // Fetch the new rowId for the inserted item
            realEstateId = this@RealEstateViewModel.mRealEstateRepository.insertRealEstate(realEstate)
            Log.w(this@RealEstateViewModel::class.java.simpleName, "insertRealEstate: id=$realEstateId")
        }
        catch (e: SQLiteConstraintException) {
            // UNIQUE constraint failed
            Log.e(this@RealEstateViewModel::class.java.simpleName, "insertRealEstate: ${e.message}")
            return@launch
        }

        // PHOTOS
        photos?.let { photos ->
            // Change the [real_estate_id] of each photo
            photos.forEach { photo ->
                photo.mRealEstateId = realEstateId
            }

            val deferred: Deferred<List<Long>> = async {
                try {
                    this@RealEstateViewModel.mPhotoRepository.insertPhotos(*photos.toTypedArray())
                }
                catch (e: SQLiteConstraintException) {
                    // UNIQUE constraint failed
                    Log.e(this@RealEstateViewModel::class.java.simpleName, "insertPhotos: ${e.message}")
                    emptyList<Long>()
                }
            }

            deferred.await().let {
                if (it.isNotEmpty()) {
                    Log.w(this@RealEstateViewModel::class.java.simpleName, "insertPhotos: ids=$it")
                }
            }
        }

        // POINTS OF INTEREST
        pointsOfInterest?.let {
            // todo - 05/04/2020 - add the points of interest
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
     * Gets all [RealEstateWithPhotos] for an [User]
     * @param userId a [Long] that contains the user Id
     * @return a [LiveData] of [List] of [RealEstateWithPhotos]
     */
    fun getRealEstatesWithPhotosByUserId(userId: Long): LiveData<List<RealEstateWithPhotos>> {
        if (this.mRealEstatesWithPhotos == null) {
            this.mRealEstatesWithPhotos = this.mRealEstateRepository.getRealEstatesWithPhotosByUserId(userId)
        }
        return this.mRealEstatesWithPhotos!!
    }

    /**
     * Gets a [RealEstateWithPhotos] by its Id
     * @param realEstateId a [Long] that contains the real estate Id
     * @return a [LiveData] of [RealEstateWithPhotos]
     */
    fun getRealEstateWithPhotosById(realEstateId: Long): LiveData<RealEstateWithPhotos> {
        if (this.mRealEstateWithPhotos == null) {
            this.mRealEstateWithPhotos = this.mRealEstateRepository.getRealEstateWithPhotosById(realEstateId)
        }
        return this.mRealEstateWithPhotos!!
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