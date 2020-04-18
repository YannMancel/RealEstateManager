package com.mancel.yann.realestatemanager.views.fragments

import android.location.Location
import androidx.annotation.LayoutRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.mancel.yann.realestatemanager.R
import com.mancel.yann.realestatemanager.models.LocationData
import com.mancel.yann.realestatemanager.models.RealEstateWithPhotos
import kotlinx.android.synthetic.main.fragment_location.view.*

/**
 * Created by Yann MANCEL on 20/02/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.views.fragments
 *
 * A [BaseFragment] subclass which implements [OnMapReadyCallback],
 * [GoogleMap.OnMarkerClickListener], [GoogleMap.OnCameraMoveStartedListener] and
 * [GoogleMap.OnCameraIdleListener].
 */
class LocationFragment : BaseFragment(),
    OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener,
    GoogleMap.OnCameraMoveStartedListener,
    GoogleMap.OnCameraIdleListener {

    // FIELDS --------------------------------------------------------------------------------------

    private var mRealEstatesWithPhotos: List<RealEstateWithPhotos>? = null
    private var mCurrentLocation: Location? = null

    private var mGoogleMap: GoogleMap? = null

    private var mIsFirstLocation: Boolean = true
    private var mIsLocatedOnUser: Boolean = true

    companion object {
        const val DEFAULT_ZOOM = 17F
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- BaseFragment --

    @LayoutRes
    override fun getFragmentLayout(): Int = R.layout.fragment_location

    override fun configureDesign() {
        // UI
        this.configureSupportMapFragment()
        this.configureListenerOfFAB()

        // LiveData
        this.configureRealEstateLiveData()
        this.configureLocationLiveData()
    }

    override fun actionAfterPermission(media: Media?) {
        when (media) {
            Media.PHOTO -> { /* Do nothing */ }
            Media.VIDEO -> { /* Do nothing */ }
            null -> this.mViewModel.startLocationUpdate()
        }
    }

    // -- Fragment --

    override fun onPause() {
        super.onPause()

        // Resets the configurations when the user navigates between Fragment
        // and go back to this Fragment
        this.mIsFirstLocation = true
        this.mIsLocatedOnUser = true
    }

    // -- OnMapReadyCallback interface --

    override fun onMapReady(googleMap: GoogleMap?) {
        this.mGoogleMap = googleMap

        this.mGoogleMap?.let {
            // Camera
            it.setOnCameraMoveStartedListener(this)
            it.setOnCameraIdleListener(this)

            // Markers
            it.setOnMarkerClickListener(this)
        }
    }

    // -- GoogleMap.OnMarkerClickListener interface --

    override fun onMarkerClick(marker: Marker?): Boolean {
        // The default behavior (return false) for a marker click event is to show its info window (if available)
        // and move the camera such that the marker is centered on the map.

        // Id from Tag
        val realEstateId = marker?.tag as? Long

        realEstateId?.let {
            // By action (Safe Args)
            val action = LocationFragmentDirections.actionLocationFragmentToDetailsFragment(it)
            this.findNavController().navigate(action)
        }

        return false
    }

    // -- GoogleMap.OnCameraMoveStartedListener interface --

    override fun onCameraMoveStarted(reason: Int) {
        when (reason) {
            // The user gestured on the map (ex: Zoom or Rotation)
            GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE -> {
                // No current location
                if (this.mCurrentLocation == null) {
                    return
                }

                // Projection's Center (visible region) = current location of user (ex: zoom or rotation)
                if (this.mGoogleMap?.projection?.visibleRegion?.latLngBounds?.center?.latitude == this.mCurrentLocation!!.latitude &&
                    this.mGoogleMap?.projection?.visibleRegion?.latLngBounds?.center?.longitude == this.mCurrentLocation!!.longitude) {
                    // Do nothing because same center
                }
                else {
                    this.mIsLocatedOnUser = false
                }
            }

            // The user tapped something on the map (ex: tap on marker)
            GoogleMap.OnCameraMoveStartedListener.REASON_API_ANIMATION -> {
                this.mIsLocatedOnUser = false
            }

            // The app moved the camera (ex: GoogleMap#moveCamera of GoogleMap#animateCamera)
            GoogleMap.OnCameraMoveStartedListener.REASON_DEVELOPER_ANIMATION -> {
                // Ignore this reason
            }
        }
    }

    // -- GoogleMap.OnCameraIdleListener interface --

    override fun onCameraIdle() {
        // When the camera has stopped moving
        // Do nothing
    }

    // -- Child Fragment --

    /**
     * Configures the child fragment which contains the Google Maps
     */
    private fun configureSupportMapFragment() {
        var childFragment = this.childFragmentManager
                                .findFragmentById(R.id.fragment_location_map_fragment) as? SupportMapFragment

        if (childFragment == null) {
            childFragment = SupportMapFragment.newInstance()

            this.childFragmentManager.beginTransaction()
                                     .add(R.id.fragment_location_map_fragment, childFragment)
                                     .commit()
        }

        childFragment?.getMapAsync(this@LocationFragment)
    }

    // -- Listener of FAB --

    /**
     * Configures the listener of FAB
     */
    private fun configureListenerOfFAB() {
        this.mRootView.fragment_location_fab.setOnClickListener {
            // Focusing on vision against the current position
            if (!this.mIsLocatedOnUser) {
                this.animateCamera()

                // Reset: Camera focus on user
                this.mIsLocatedOnUser = true
            }
        }
    }

    // -- LiveData --

    /**
     * Configures the LiveData thanks to a simple format
     */
    private fun configureRealEstateLiveData() {
        // todo - 14/04/2020 - Next feature: Add user's authentication instead of 1L
        this.mViewModel
            .getRealEstatesWithPhotosByUserId(userId = 1L)
            .observe(
                this.viewLifecycleOwner,
                Observer {
                    this.mRealEstatesWithPhotos = it
                    this.addPointsOfInterest(it)
                }
            )
    }

    /**
     * Configures the [LiveData] of [LocationData]
     */
    private fun configureLocationLiveData() {
        this.mViewModel
            .getLocation(this.requireContext())
            .observe(
                this@LocationFragment.viewLifecycleOwner,
                Observer { this@LocationFragment.onChangedLocationData(it) }
            )
    }

    // -- LocationData --

    /**
     * Method to replace the {@link androidx.lifecycle.Observer} of {@link LocationData}
     * @param locationData a {@link LocationData}
     */
    private fun onChangedLocationData (locationData: LocationData) {
        // Exception
        if (this.handleLocationException(locationData.mException)) {
            return
        }

        // No Location
        if (locationData.mLocation == null) {
            return
        }

        // To avoid the NullPointerException during the rotation screen for example
        if (this.mGoogleMap == null) {
            return
        }

        // Update current location
        this.mCurrentLocation = locationData.mLocation

        // Focus on the current location of user
        if (this.mIsLocatedOnUser) {
            // First Location
            if (this.mIsFirstLocation) {
                this.mIsFirstLocation = false

                // This method is put in place here and not in onMapReady method
                // because the MyLocation parameter of GoogleMap must have the permission.ACCESS_FINE_LOCATION.
                // This permission is already asked with LocationLiveData.
                this.configureGoogleMapStyle()

                this.configureCamera()
            }
            else {
                this.animateCamera()
            }

            // Add the points of interest
            this.mRealEstatesWithPhotos?.let {
                this.addPointsOfInterest(it)
            }
        }
    }

    // -- POIs --

    /**
     * Adds the points of interest
     */
    private fun addPointsOfInterest(realEstateWithPhotos: List<RealEstateWithPhotos>) {
        // To avoid the NullPointerException during the rotation screen for example
        this.mGoogleMap?.let {
            // Remove Markers
            it.clear()

            realEstateWithPhotos.forEach { poi ->
                val location = LatLng(
                    poi.mRealEstate?.mAddress?.mLatitude!!,
                    poi.mRealEstate?.mAddress?.mLongitude!!
                )

                // Adds Markers
                val marker = it.addMarker(
                    MarkerOptions()
                        .position(location)
                        .title("${poi.mRealEstate?.mType}")
                        .icon(BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_BLUE)
                        )
                )

                // Add tag -> Real Estate Id
                marker?.tag = poi.mRealEstate?.mId
            }
        }
    }

    // -- Google Maps --

    /**
     * Configures the style of the [GoogleMap]
     */
    private fun configureGoogleMapStyle() {
        // STYLE
        // todo: 07/04/2020 - Add style map

        this.mGoogleMap?.let {
            // GESTURES
            it.uiSettings?.isZoomGesturesEnabled = true
            it.uiSettings?.isRotateGesturesEnabled = true

            // SCROLL
            it.uiSettings?.isScrollGesturesEnabled = true
            it.uiSettings?.isScrollGesturesEnabledDuringRotateOrZoom = true

            // MIN ZOOM LEVELS
            it.setMinZoomPreference(if (it.minZoomLevel > 10.0F) it.minZoomLevel else 10.0F)

            // MAX ZOOM LEVELS
            it.setMaxZoomPreference(if (it.maxZoomLevel < 21.0F) it.maxZoomLevel else 21.0F)

            // MY LOCATION
            it.isMyLocationEnabled = true
            it.uiSettings?.isMyLocationButtonEnabled = false

            // TOOLBAR
            it.uiSettings?.isMapToolbarEnabled = false
        }
    }

    /**
     * Configures the camera of [GoogleMap]
     */
    private fun configureCamera() {
        // No current location
        if (this.mCurrentLocation == null) {
            return
        }

        // Location to LatLng -> Target
        val target = LatLng(
            this.mCurrentLocation!!.latitude,
            this.mCurrentLocation!!.longitude
        )

        // Camera
        this.mGoogleMap?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                target,
                DEFAULT_ZOOM
            )
        )
    }

    /**
     * Animates the camera of [GoogleMap]
     */
    private fun animateCamera() {
        // No current location
        if (this.mCurrentLocation == null) {
            return
        }

        // Location to LatLng -> Target
        val target = LatLng(
            this.mCurrentLocation!!.latitude,
            this.mCurrentLocation!!.longitude
        )

        // CameraPosition
        val cameraPosition = CameraPosition.Builder()
                                           .target(target)
                                           .zoom(DEFAULT_ZOOM)
                                           .build()

        // Camera
        this.mGoogleMap?.animateCamera(
            CameraUpdateFactory.newCameraPosition(
                cameraPosition
            )
        )
    }
}