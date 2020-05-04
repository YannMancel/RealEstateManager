package com.mancel.yann.realestatemanager.liveDatas

import androidx.lifecycle.LiveData
import com.mancel.yann.realestatemanager.models.PointOfInterest
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import timber.log.Timber

/**
 * Created by Yann MANCEL on 15/04/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.liveDatas
 *
 * A [LiveData] of [List] of [PointOfInterest] subclass.
 */
class POIsSearchLiveData : LiveData<List<PointOfInterest>>() {

    // FIELDS --------------------------------------------------------------------------------------

    private var mDisposable: Disposable? = null
    private val mPOIs = mutableListOf<PointOfInterest>()
    private val mAlreadySelectedPOIs = mutableListOf<PointOfInterest>()

    // METHODS -------------------------------------------------------------------------------------

    // -- LiveData --

    override fun onInactive() {
        super.onInactive()

        // Disposes the Disposable
        this.mDisposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
    }

    // -- Points of interest --

    /**
     * Gets the POIs with an [Single]
     * @param single a [Single] of [List] of [PointOfInterest]
     */
    fun getPOIsSearchWithSingle(single: Single<List<PointOfInterest>>) {
        // Creates stream
        this.mDisposable = single.subscribeWith(object : DisposableSingleObserver<List<PointOfInterest>>() {

            override fun onSuccess(result: List<PointOfInterest>) {
                with(this@POIsSearchLiveData.mPOIs) {
                    clear()
                    addAll(result)
                }

                // Add current POIs if possible
                if (this@POIsSearchLiveData.mAlreadySelectedPOIs.isNotEmpty()) {
                    this@POIsSearchLiveData.mAlreadySelectedPOIs.forEach { poiFromDB ->
                        // Search if already present
                        val index = this@POIsSearchLiveData.mPOIs.indexOfFirst {
                            it.mName == poiFromDB.mName &&
                                    it.mAddress?.mLatitude == poiFromDB.mAddress?.mLatitude &&
                                    it.mAddress?.mLongitude == poiFromDB.mAddress?.mLongitude
                        }

                        if (index != -1) {
                            this@POIsSearchLiveData.mPOIs[index].mIsSelected = true
                        }
                        else {
                            this@POIsSearchLiveData.mPOIs.add(poiFromDB)
                        }
                    }
                }

                // Notify
                this@POIsSearchLiveData.value = this@POIsSearchLiveData.mPOIs
            }

            override fun onError(e: Throwable) {
                Timber.e("onError: ${e.message}")
            }
        })
    }

    /**
     * Adds all current [PointOfInterest]
     * @param poiList a [List] of [PointOfInterest]
     */
    fun addCurrentPOIs(poiList: List<PointOfInterest>) {
        // MODE EDIT
        with(this.mAlreadySelectedPOIs) {
            clear()
            addAll(poiList)
        }

        // Add POIs if possible
        this.mAlreadySelectedPOIs.forEach { poiFromDB ->
            // Search if already present
            val index = this.mPOIs.indexOfFirst {
                it.mName == poiFromDB.mName &&
                it.mAddress?.mLatitude == poiFromDB.mAddress?.mLatitude &&
                it.mAddress?.mLongitude == poiFromDB.mAddress?.mLongitude
            }

            if (index != -1) {
                this.mPOIs[index].mIsSelected = true
            }
            else {
                this.mPOIs.add(poiFromDB)
            }
        }

        // Notify
        this.value = this.mPOIs
    }

    /**
     * Checks if the [PointOfInterest] is selected
     * @param poi a [PointOfInterest]
     */
    fun checkPOI(poi: PointOfInterest) {
        this.mPOIs.forEach {
            if (it == poi) {
                it.mIsSelected = !poi.mIsSelected
            }
        }

        // Notify
        this.value = this.mPOIs
    }

    /**
     * Gets all selected [PointOfInterest]
     */
    fun getSelectedPOIs(): List<PointOfInterest> = this.mPOIs.filter { it.mIsSelected }

    /**
     * Gets just new selected [PointOfInterest]
     */
    // todo: 17/04/2020 - Remove it when the RealEstateViewModel#updateRealEstate method will be update
    fun getJustNewSelectedPOIs(): List<PointOfInterest> =
        this.mPOIs.filter { it.mIsSelected && it.mId == 0L}
}