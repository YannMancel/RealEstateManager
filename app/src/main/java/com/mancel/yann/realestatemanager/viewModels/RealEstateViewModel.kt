package com.mancel.yann.realestatemanager.viewModels

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mancel.yann.realestatemanager.R
import com.mancel.yann.realestatemanager.liveDatas.LocationLiveData
import com.mancel.yann.realestatemanager.liveDatas.POIsSearchLiveData
import com.mancel.yann.realestatemanager.liveDatas.PhotoCreatorLiveData
import com.mancel.yann.realestatemanager.models.*
import com.mancel.yann.realestatemanager.notifications.RealEstateNotification
import com.mancel.yann.realestatemanager.repositories.*
import com.mancel.yann.realestatemanager.utils.SaveTools
import com.mancel.yann.realestatemanager.views.dialogs.SettingsDialogFragment
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
    private val mPlaceRepository: PlaceRepository,
    private val mUserRepository: UserRepository,
    private val mRealEstateRepository: RealEstateRepository,
    private val mPhotoRepository: PhotoRepository,
    private val mPointOfInterestRepository: PointOfInterestRepository,
    private val mRealEstatePointOfInterestCrossRefRepository: RealEstatePointOfInterestCrossRefRepository
) : ViewModel() {

    // FIELDS --------------------------------------------------------------------------------------

    private var mLocation: LocationLiveData? = null

    private var mUser: LiveData<User>? = null

    private var mRealEstatesWithPhotos: LiveData<List<RealEstateWithPhotos>>? = null
    private var mRealEstateWithPhotos: LiveData<RealEstateWithPhotos>? = null
    private var mRealEstateWithPointsOfInterest: LiveData<RealEstateWithPointsOfInterest>? = null
    private var mMultiSearch: LiveData<List<RealEstateWithPhotos>>? = null

    private var mPhotos: LiveData<List<Photo>>? = null
    private var mPhotoCreator: PhotoCreatorLiveData? = null

    private var mPOIs: LiveData<List<PointOfInterest>>? = null
    private var mPOIsSearch: POIsSearchLiveData? = null

    // METHODS -------------------------------------------------------------------------------------

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
            this.mRealEstateWithPhotos =
                this.mRealEstateRepository.getRealEstateWithPhotosById(realEstateId)
        }
        return this.mRealEstateWithPhotos!!
    }

    /**
     * Gets a [RealEstateWithPointsOfInterest] by its Id
     * @param realEstateId a [Long] that contains the real estate Id
     * @return a [LiveData] of [RealEstateWithPointsOfInterest]
     */
    fun getRealEstateWithPointsOfInterestById(
        realEstateId: Long
    ): LiveData<RealEstateWithPointsOfInterest> {
        if (this.mRealEstateWithPointsOfInterest == null) {
            this.mRealEstateWithPointsOfInterest =
                this.mRealEstateRepository.getRealEstateWithPointsOfInterestById(realEstateId)
        }
        return this.mRealEstateWithPointsOfInterest!!
    }

    /**
     * Gets all [RealEstateWithPhotos] by multi search
     * @param realEstateId a [Long] that contains the real estate Id
     * @return a [LiveData] of [RealEstateWithPhotos]
     */
    fun getRealEstatesWithPhotosByMultiSearch(
        minPrice: Double = 0.0,
        maxPrice: Double = Double.MAX_VALUE,
        minSurface: Double = 0.0,
        maxSurface: Double = Double.MAX_VALUE,
        minNumberRoom: Int = 0,
        maxNumberRoom: Int = Int.MAX_VALUE
    ): LiveData<List<RealEstateWithPhotos>> {
        if (this.mMultiSearch == null) {
            this.mMultiSearch = this.mRealEstateRepository.getRealEstatesWithPhotosByMultiSearch(
                minPrice,
                maxPrice,
                minSurface,
                maxSurface,
                minNumberRoom,
                maxNumberRoom
            )
        }
        return this.mMultiSearch!!
    }

    /**
     * Removes all observers of this [LiveData]
     * @param owner a [LifecycleOwner]
     */
    fun removeObserversOfMultiSearch(owner: LifecycleOwner) {
        this.mMultiSearch?.removeObservers(owner)
    }

    /**
     * Reset of this [LiveData]
     */
    fun resetMultiSearch() {
        this.mMultiSearch = null
    }

    /**
     * Gets this [LiveData] even if it is null
     */
    fun getMultiSearchEvenIfNull(): LiveData<List<RealEstateWithPhotos>>? = this.mMultiSearch

    /**
     * Inserts the new [RealEstate] in argument
     * @param context           a [Context]
     * @param realEstate        a [RealEstate]
     * @param photos            a [List] of [Photo]
     * @param pointsOfInterest  a [List] of [PointOfInterest]
     */
    fun insertRealEstate(
        context: Context,
        realEstate: RealEstate,
        photos: List<Photo>? = null,
        pointsOfInterest: List<PointOfInterest>? = null
    ) = viewModelScope.launch(Dispatchers.IO) {
        val realEstateId: Long

        // INSERT: Real Estate
        try {
            // Fetch the new rowId for the inserted item
            realEstateId = this@RealEstateViewModel.mRealEstateRepository.insertRealEstate(realEstate)

            // From SharedPreferences
            val isEnableNotification = SaveTools.fetchBooleanFromSharedPreferences(
                context,
                SettingsDialogFragment.BUNDLE_SWITCH_NOTIFICATION
            )

            // NOTIFICATION
            if (isEnableNotification) {
                RealEstateNotification.sendVisualNotification(
                    context,
                    context.getString(
                        R.string.notification_message,
                        realEstate.mType, realEstate.mPrice
                    )
                )
            }
        }
        catch (e: SQLiteConstraintException) {
            // UNIQUE constraint failed
            Timber.e("insertRealEstate: ${e.message}")
            return@launch
        }

        // INSERT: Photos
        this@RealEstateViewModel.insertPhotosWithRealEstateId(
            photos,
            realEstateId
        )

        // INSERT: Points Of Interest
        this@RealEstateViewModel.insertPOIsWithRealEstateId(
            pointsOfInterest,
            realEstateId
        )
    }

    /**
     * Updates a [RealEstate] in argument
     * @param realEstate        a [RealEstate]
     * @param oldPhotos         a [List] of [Photo]
     * @param newPhotos         a [List] of [Photo]
     * @param pointsOfInterest  a [List] of [PointOfInterest]
     */
    fun updateRealEstate(
        realEstate: RealEstate,
        oldPhotos: List<Photo>? = null,
        newPhotos: List<Photo>? = null,
        pointsOfInterest: List<PointOfInterest>? = null
    ) = viewModelScope.launch(Dispatchers.IO) {
        // UPDATE: Real Estate
        try {
            // Fetch the number of updated row
            val  numberOfUpdatedRow = this@RealEstateViewModel.mRealEstateRepository.updateRealEstate(realEstate)

            // Update impossible
            if (numberOfUpdatedRow == 0) {
                Timber.e("updateRealEstate: Update impossible, Number of update row = $numberOfUpdatedRow")
                return@launch
            }
        }
        catch (e: SQLiteConstraintException) {
            // UNIQUE constraint failed
            Timber.e("updateRealEstate: ${e.message}")
            return@launch
        }

        // UPDATE: Photos
        this@RealEstateViewModel.updatePhotosWithRealEstateId(
            oldPhotos,
            newPhotos,
            realEstate.mId
        )

        // NO UPDATE JUST INSERT: Points Of Interest
        // todo: 17/04/2020 - Update with SQL request
        this@RealEstateViewModel.insertPOIsWithRealEstateId(
            pointsOfInterest,
            realEstate.mId
        )
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
     * Add a [List] of [Photo] into [PhotoCreatorLiveData]
     * @param photos a [List] of [Photo]
     */
    fun addCurrentPhotos(photos: List<Photo>) = this.mPhotoCreator?.addCurrentPhotos(photos)

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
        photos?.let { photos ->
            // Change the [real_estate_id] of each photo
            photos.forEach { photo ->
                photo.mRealEstateId = realEstateId
            }

            // INSERT: Photos
            val deferred: Deferred<List<Long>> = async(start = CoroutineStart.LAZY) {
                try {
                    this@RealEstateViewModel.mPhotoRepository.insertPhotos(*photos.toTypedArray())
                }
                catch (e: SQLiteConstraintException) {
                    // UNIQUE constraint failed
                    Timber.e("insertPhotos: ${e.message}")
                    emptyList<Long>()
                }
            }

            // Lazily started async
            deferred.start()
        }
    }

    /**
     * Updates several [Photo] into database
     * @param oldPhotos     a [List] of [Photo]
     * @param newPhotos     a [List] of [Photo]
     * @param realEstateId  a [Long] that contains the real estate Id
     */
    private suspend fun updatePhotosWithRealEstateId(
        oldPhotos: List<Photo>? = null,
        newPhotos: List<Photo>? = null,
        realEstateId: Long
    ) = withContext(Dispatchers.IO) {
        newPhotos?.let { newPhotos ->

            // DELETE: Photos
            oldPhotos?.let { oldPhotos ->
                val photosToDelete = oldPhotos.filterNot { oldPhoto ->
                    newPhotos.any { newPhoto ->
                        oldPhoto.mUrlPicture == newPhoto.mUrlPicture
                    }
                }

                if (!photosToDelete.isNullOrEmpty()) {
                    photosToDelete.forEach {
                        // DELETE: Photos
                        val deferred: Deferred<Int> = async(start = CoroutineStart.LAZY) {
                            try {
                                this@RealEstateViewModel.mPhotoRepository.deletePhoto(it)
                            }
                            catch (e: SQLiteConstraintException) {
                                // UNIQUE constraint failed
                                Timber.e("deletePhoto: ${e.message}")
                                0
                            }
                        }

                        // Lazily started async
                        deferred.start()
                    }
                }
            }

            // UPDATE: Photos
            val photosToUpdate = newPhotos.filter { it.mId != 0L }

            if (!photosToUpdate.isNullOrEmpty()) {
                photosToUpdate.forEach {
                    // UPDATE: Photos
                    val deferred: Deferred<Int> = async(start = CoroutineStart.LAZY) {
                        try {
                            this@RealEstateViewModel.mPhotoRepository.updatePhoto(it)
                        }
                        catch (e: SQLiteConstraintException) {
                            // UNIQUE constraint failed
                            Timber.e("updatePhotos: ${e.message}")
                            0
                        }
                    }

                    // Lazily started async
                    deferred.start()
                }
            }

            // INSERT: Photos
            val photosToInsert = newPhotos.filter { it.mId == 0L }

            if (!photosToInsert.isNullOrEmpty()) {
                this@RealEstateViewModel.insertPhotosWithRealEstateId(
                    photosToInsert,
                    realEstateId
                )
            }
        }
    }

    // -- Points of interest --

    /**
     * Gets all [PointOfInterest]
     * @return a [LiveData] of [List] of [PointOfInterest]
     */
    fun getPOIs(): LiveData<List<PointOfInterest>> {
        if (this.mPOIs == null) {
            this.mPOIs = this.mPointOfInterestRepository.getAllPointsOfInterest()
        }
        return this.mPOIs!!
    }

    /**
     * Gets the [LiveData] of [List] of [PointOfInterest]
     * @param context   a [Context]
     * @param latitude  a [Double] that contains the latitude value
     * @param longitude a [Double] that contains the longitude value
     * @param radius    a [Double] that contains the radius value
     * @param types     a [String] that contains the types
     * @return a [LiveData] of [List] of [PointOfInterest]
     */
    fun getPOIsSearch(
        context: Context? = null,
        latitude: Double? = null,
        longitude: Double? = null,
        radius: Double? = null,
        types: String? = null
    ): LiveData<List<PointOfInterest>> {
        if (this.mPOIsSearch == null) {
            this.mPOIsSearch = POIsSearchLiveData()
        }

        // Fetch the data
        context?.let {
            this.fetchPOIsSearch(context, latitude!!, longitude!!, radius!!, types!!)
        }

        return this.mPOIsSearch!!
    }

    /**
     * Fetches a [List] of [PointOfInterest]
     * @param context   a [Context]
     * @param latitude  a [Double] that contains the latitude value
     * @param longitude a [Double] that contains the longitude value
     * @param radius    a [Double] that contains the radius value
     * @param types     a [String] that contains the types
     */
    fun fetchPOIsSearch(
        context: Context,
        latitude: Double,
        longitude: Double,
        radius: Double,
        types: String
    ) {
        // Single
        val single = this.mPlaceRepository.getStreamToFetchPointsOfInterest(
            location = "$latitude,$longitude",
            radius = radius,
            types = types,
            key = context.resources.getString(R.string.google_maps_key)
        )

        // Updates LiveData
        this.mPOIsSearch?.getPOIsSearchWithSingle(single)
    }

    /**
     * Adds all current [PointOfInterest]
     * @param poiList a [List] of [PointOfInterest]
     */
    fun addCurrentPOIs(poiList: List<PointOfInterest>) = this.mPOIsSearch?.addCurrentPOIs(poiList)

    /**
     * Checks if the [PointOfInterest] is selected
     * @param poi a [PointOfInterest]
     */
    fun checkPOI(poi: PointOfInterest) = this.mPOIsSearch?.checkPOI(poi)

    /**
     * Gets all selected [PointOfInterest]
     */
    fun getSelectedPOIs() =  this.mPOIsSearch?.getSelectedPOIs()

    /**
     * Gets just new selected [PointOfInterest]
     */
    // todo: 17/04/2020 - Remove it when the RealEstateViewModel#updateRealEstate method will be update
    fun getJustNewSelectedPOIs() =  this.mPOIsSearch?.getJustNewSelectedPOIs()

    /**
     * Inserts several [PointOfInterest] into database
     * @param pointsOfInterest  a [List] of [PointOfInterest]
     * @param realEstateId      a [Long] that contains the real estate Id
     */
    private suspend fun insertPOIsWithRealEstateId(
        pointsOfInterest: List<PointOfInterest>?,
        realEstateId: Long
    ) = withContext(Dispatchers.IO) {

        /*
            + -> Fetch all POIs from database (Data must fetch with Fragment)
            |
            + -> Loop on each POI of List in argument (User's choice)
                 |
                 + -> INSERT POI
                      |
                      + -> Yes (new poi Id) -> INSERT Cross Ref
                      |
                      + -> No (poiId == 0L) -> Search a POI that match with the same data
                                               |
                                               + -> Yes (poi Id) -> INSERT Cross Ref
                                               |
                                               + -> No -> Do nothing
         */

        pointsOfInterest?.let {
            // FETCH: All Points Of Interest
            // [Warning] To fetch LiveData's value, the current Fragment must observe this LiveData
            val allPOIsFromDB = this@RealEstateViewModel.mPOIs?.value ?: emptyList()

            // Action on each POI from argument
            pointsOfInterest.forEach { poi ->
                // INSERT: Point Of Interest
                val deferred: Deferred<Long> = async {
                    try {
                        this@RealEstateViewModel
                            .mPointOfInterestRepository
                            .insertPointOfInterest(poi)
                    } catch (e: SQLiteConstraintException) {
                        // UNIQUE constraint failed
                        Timber.e("insertPointOfInterest: ${e.message}")
                        0L
                    }
                }

                deferred.await().let { poiId ->
                    // Insert impossible
                    if (poiId == 0L) {
                        // Only one POI so filteredPOIs.size == 1
                        val filteredPOIs = allPOIsFromDB.filter {
                            it.mName == poi.mName &&
                                    it.mAddress!!.mLatitude == poi.mAddress!!.mLatitude &&
                                    it.mAddress!!.mLongitude == poi.mAddress!!.mLongitude
                        }

                        if (!filteredPOIs.isNullOrEmpty() && filteredPOIs.size == 1) {
                            // INSERT: Cross Ref
                            this@RealEstateViewModel.insertRealEstatePointOfInterestCrossRef(
                                realEstateId,
                                filteredPOIs[0].mId
                            )
                        }
                        else {
                            Timber.e("Error: Unique indices")
                        }
                    }
                    else {
                        // INSERT: Cross Ref
                        this@RealEstateViewModel.insertRealEstatePointOfInterestCrossRef(
                            realEstateId,
                            poiId
                        )
                    }
                }
            }
        }
    }

    // -- RealEstatePointOfInterestCrossRef --

    /**
     * Inserts a [insertRealEstatePointOfInterestCrossRef] into database
     * @param realEstateId      a [Long] that contains the [RealEstate] Id
     * @param PointOfInterestId a [Long] that contains the [PointOfInterest] Id
     */
    private suspend fun insertRealEstatePointOfInterestCrossRef(
        realEstateId: Long,
        PointOfInterestId: Long
    ) = withContext(Dispatchers.IO) {
        // INSERT: Cross Ref
        val deferred: Deferred<Long> = async(start = CoroutineStart.LAZY) {
            try {
                this@RealEstateViewModel
                    .mRealEstatePointOfInterestCrossRefRepository
                    .insertCrossRef(
                        RealEstatePointOfInterestCrossRef(
                            mRealEstateId = realEstateId,
                            mPointOfInterestId = PointOfInterestId
                        )
                    )
            }
            catch (e: SQLiteConstraintException) {
                // UNIQUE constraint failed
                Timber.e("insertCrossRef: ${e.message}")
                0L
            }
        }
        // Lazily started async
        deferred.start()
    }
}