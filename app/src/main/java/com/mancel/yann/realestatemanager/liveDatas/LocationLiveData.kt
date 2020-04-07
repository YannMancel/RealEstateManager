package com.mancel.yann.realestatemanager.liveDatas

import android.content.Context
import android.os.Looper
import androidx.lifecycle.LiveData
import com.google.android.gms.location.*
import com.mancel.yann.realestatemanager.models.LocationData

/**
 * Created by Yann MANCEL on 07/04/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.liveDatas
 *
 * A [LiveData] of [LocationData] subclass.
 */
class LocationLiveData(
    val context: Context
): LiveData<LocationData>() {

    // FIELDS --------------------------------------------------------------------------------------

    private val mContext: Context = context.applicationContext
    private var isFirstSubscriber: Boolean = true

    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var mLocationRequest: LocationRequest
    private lateinit var mLocationCallback: LocationCallback

    // CONSTRUCTORS --------------------------------------------------------------------------------

    init {
        this.configureFusedLocationProviderClient()
        this.configureLocationRequest()
        this.configureLocationCallback()
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- LiveData --

    override fun onActive() {
        super.onActive()

        if (this.isFirstSubscriber) {
            this.requestLastLocation()
            this.requestUpdateLocation()

            // For the other subscribers
            this.isFirstSubscriber = false
        }
    }

    override fun onInactive() {
        super.onInactive()

        this.mFusedLocationProviderClient.removeLocationUpdates(this.mLocationCallback)

        // For the other subscribers
        this.isFirstSubscriber = true
    }

    // -- FusedLocationProviderClient --

    /**
     * Configure the [FusedLocationProviderClient]
     */
    private fun configureFusedLocationProviderClient() {
        this.mFusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this.mContext)
    }

    // -- LocationRequest --
    /**
     * Creates the [LocationRequest]
     */
    private fun configureLocationRequest() {
        this.mLocationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    // -- LocationCallback --
    /**
     * Configures the [LocationCallback]
     */
    private fun configureLocationCallback() {
        this.mLocationCallback = object : LocationCallback() {

            override fun onLocationResult(locationResult: LocationResult) {

                for (location in locationResult.locations) {
                    val latitude = location.latitude
                    val longitude = location.longitude

                    // Error: Out of boundaries of coordinates
                    if (!(latitude >= -90.0 && latitude <= 90.0 && longitude >= -180.0 && longitude <= 180.0)) {
                        return
                    }

                    // Not useful: Same LocationData
                    this@LocationLiveData.value?.let { locationData ->
                        locationData.mLocation?.let {
                            if (it.latitude == latitude && it.longitude == longitude) {
                                return
                            }
                        }
                    }

                    // Notify
                    this@LocationLiveData.value = LocationData(mLocation = location)
                }
            }
        }
    }

    // -- Request location --

    /**
     * Requests the last location thanks to [FusedLocationProviderClient]
     */
    private fun requestLastLocation() {
        this.mFusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location ->
                // Got last known location. In some rare situations this can be null.
                location?.let {
                    // Notify
                    this.value = LocationData(mLocation = it)
                }
            }
            .addOnFailureListener { exception ->
                // Notify
                this.value = LocationData(mException = exception)
            }
    }

    /**
     * Requests the update location
     */
    fun requestUpdateLocation() {
        val builder = LocationSettingsRequest.Builder()
                                             .addLocationRequest(this.mLocationRequest)

        val client = LocationServices.getSettingsClient(this.mContext)

        // TASK: Task<LocationSettingsResponse>
        client.checkLocationSettings(builder.build())
              .addOnSuccessListener {
                  this.requestLocation()
              }
              .addOnFailureListener { exception ->
                  // Notify
                  this.value = LocationData(mException = exception)
              }
    }

    /**
     * Requests the location to create the looper
     */
    private fun requestLocation() {
        this.mFusedLocationProviderClient.requestLocationUpdates(
            this.mLocationRequest,
            this.mLocationCallback,
            Looper.getMainLooper()
        )
            .addOnFailureListener { exception ->
                // Notify
                this.value = LocationData(mException = exception)
            }
    }
}