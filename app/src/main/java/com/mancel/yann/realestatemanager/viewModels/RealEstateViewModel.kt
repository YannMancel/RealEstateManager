package com.mancel.yann.realestatemanager.viewModels

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mancel.yann.realestatemanager.liveDatas.LocationLiveData
import com.mancel.yann.realestatemanager.liveDatas.PhotoCreatorLiveData
import com.mancel.yann.realestatemanager.models.*
import com.mancel.yann.realestatemanager.repositories.PhotoRepository
import com.mancel.yann.realestatemanager.repositories.RealEstateRepository
import com.mancel.yann.realestatemanager.repositories.UserRepository
import kotlinx.coroutines.*
import timber.log.Timber

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

    private var mPhotos: LiveData<List<Photo>>? = null
    private var mPhotoCreator: PhotoCreatorLiveData? = null

    // CONSTRUCTORS --------------------------------------------------------------------------------

    init {
        Timber.d("RealEstateViewModel: INIT")
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- ViewModel --

    override fun onCleared() {
        super.onCleared()
        Timber.d("RealEstateViewModel: onCleared")
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
            Timber.d("insertUser: Id = $userId")
        }
        catch (e: SQLiteConstraintException) {
            // UNIQUE constraint failed
            Timber.e("insertUser: ${e.message}")
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
            Timber.d("insertRealEstate: Id = $realEstateId")
        }
        catch (e: SQLiteConstraintException) {
            // UNIQUE constraint failed
            Timber.e("insertRealEstate: ${e.message}")
            return@launch
        }

        // PHOTOS
        this@RealEstateViewModel.insertPhotosWithRealEstateId(
            photos,
            realEstateId
        )

        // POINTS OF INTEREST
        pointsOfInterest?.let {
            // todo - 05/04/2020 - add the points of interest
        }
    }

    /**
     * Updates a [RealEstate] in argument
     * @param realEstate        a [RealEstate]
     * @param photos            a [List] of [Photo]
     * @param pointsOfInterest  a [List] of [PointOfInterest]
     */
    fun updateRealEstate(
        realEstate: RealEstate,
        photos: List<Photo>? = null,
        pointsOfInterest: List<PointOfInterest>? = null
    ) = viewModelScope.launch(Dispatchers.IO) {
        // REAL ESTATE
        try {
            // Fetch the number of updated row
            val  numberOfUpdatedRow = this@RealEstateViewModel.mRealEstateRepository.updateRealEstate(realEstate)
            Timber.d("updateRealEstate: Number of update row = $numberOfUpdatedRow")

            // Update impossible
            if (numberOfUpdatedRow == 0) {
                return@launch
            }
        }
        catch (e: SQLiteConstraintException) {
            // UNIQUE constraint failed
            Timber.e("updateRealEstate: ${e.message}")
            return@launch
        }

        // PHOTOS
        this@RealEstateViewModel.insertPhotosWithRealEstateId(
            photos,
            realEstate.mId
        )

        // POINTS OF INTEREST
        pointsOfInterest?.let {
            // todo - 13/04/2020 - add the points of interest
        }
    }

    // -- Photo --

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

    /**
     * Add a [Photo] into [PhotoCreatorLiveData]
     * @param photo a [Photo]
     */
    fun addPhotoToPhotoCreator(photo: Photo) = this.mPhotoCreator?.addPhoto(photo)

    /**
     * Updates a [Photo] into [PhotoCreatorLiveData]
     * @param photo a [Photo]
     */
    fun updatePhotoToPhotoCreator(photo: Photo) = this.mPhotoCreator?.updatePhoto(photo)

    /**
     * Deletes a [Photo] into [PhotoCreatorLiveData]
     * @param photo a [Photo]
     */
    fun deletePhotoToPhotoCreator(photo: Photo) = this.mPhotoCreator?.deletePhoto(photo)

    /**
     * Inserts several [Photo] into database
     * @param photos        a [List] of [Photo]
     * @param realEstateId  a [Long] that contains the real estate Id
     */
    private suspend fun insertPhotosWithRealEstateId(
        photos: List<Photo>?,
        realEstateId: Long
    ) = withContext(Dispatchers.IO) {
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
                    Timber.e("insertPhotos: ${e.message}")
                    emptyList<Long>()
                }
            }

            deferred.await().let {
                if (it.isNotEmpty()) {
                    Timber.d("insertPhotos: Ids = $it")
                }
            }
        }
    }
}