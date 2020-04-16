package com.mancel.yann.realestatemanager.liveDatas

import androidx.lifecycle.LiveData
import com.mancel.yann.realestatemanager.models.PointOfInterest
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
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
    private val mPOIs: MutableList<PointOfInterest> = mutableListOf()

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
     * Gets the POIs with an [Observable]
     * @param observable an [Observable] of [List] of [PointOfInterest]
     */
    fun getPOIsSearchWithObservable(observable: Observable<List<PointOfInterest>>) {
        // Creates stream
        this.mDisposable = observable.subscribeWith(object : DisposableObserver<List<PointOfInterest>>() {

            override fun onNext(result: List<PointOfInterest>) {
                with(this@POIsSearchLiveData.mPOIs) {
                    clear()
                    addAll(result)
                }

                // Notify
                this@POIsSearchLiveData.value = result
            }

            override fun onError(e: Throwable) {
                Timber.e("onError: ${e.message}")
            }

            override fun onComplete() { /* Do nothing */ }
        })
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
    fun getSelectedPOIs() =  this.mPOIs.filter { it.mIsSelected }
}