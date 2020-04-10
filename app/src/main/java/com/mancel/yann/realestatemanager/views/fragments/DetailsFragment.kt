package com.mancel.yann.realestatemanager.views.fragments

import android.view.View
import androidx.annotation.LayoutRes
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mancel.yann.realestatemanager.R
import com.mancel.yann.realestatemanager.models.RealEstateWithPhotos
import com.mancel.yann.realestatemanager.views.adapters.AdapterListener
import com.mancel.yann.realestatemanager.views.adapters.PhotoAdapter
import kotlinx.android.synthetic.main.fragment_details.view.*

/**
 * Created by Yann MANCEL on 20/02/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.views.fragments
 *
 * A [BaseFragment] subclass which implements [AdapterListener] and [OnMapReadyCallback].
 */
class DetailsFragment : BaseFragment(), AdapterListener, OnMapReadyCallback {

    // FIELDS --------------------------------------------------------------------------------------

    private val mItemId: Long by lazy {
        DetailsFragmentArgs.fromBundle(this.requireArguments()).itemId
    }

    private lateinit var mAdapter: PhotoAdapter
    private var mGoogleMap: GoogleMap? = null

    // METHODS -------------------------------------------------------------------------------------

    // -- BaseFragment --

    @LayoutRes
    override fun getFragmentLayout(): Int = R.layout.fragment_details

    override fun configureDesign() {
        // UI
        this.configureRecyclerView()
        this.configureSupportMapFragment()

        // LiveData
        this.configureRealEstateLiveData()
    }

    // -- AdapterListener interface --

    override fun onDataChanged() {
        this.mRootView.fragment_details_no_data.visibility = if (this.mAdapter.itemCount == 0)
                                                                 View.VISIBLE
                                                             else
                                                                 View.GONE
    }

    override fun onClick(v: View?) { /* Do nothing */ }

    // -- OnMapReadyCallback interface --

    override fun onMapReady(googleMap: GoogleMap?) {
        this.mGoogleMap = googleMap

        // TOOLBAR
        this.mGoogleMap?.uiSettings?.isMapToolbarEnabled = false
    }

    // -- RecyclerView --

    /**
     * Configures the [RecyclerView]
     */
    private fun configureRecyclerView() {
        // Adapter
        this.mAdapter = PhotoAdapter(mCallback = this@DetailsFragment)

        // LayoutManager
        val viewManager = LinearLayoutManager(this.requireContext(),
                                              LinearLayoutManager.HORIZONTAL,
                                             false)

        // Divider
        val divider = DividerItemDecoration(this.requireContext(),
                                            DividerItemDecoration.HORIZONTAL)

        // RecyclerView
        with(this.mRootView.fragment_details_RecyclerView) {
            setHasFixedSize(true)
            layoutManager = viewManager
            addItemDecoration(divider)
            adapter = mAdapter
        }
    }

    // -- Child Fragment --

    /**
     * Configures the child fragment which contains the Google Maps
     */
    private fun configureSupportMapFragment() {
        var childFragment = this.childFragmentManager
                                .findFragmentById(R.id.fragment_details_map_lite_mode) as? SupportMapFragment

        if (childFragment == null) {
            childFragment = SupportMapFragment.newInstance()

            this.childFragmentManager.beginTransaction()
                                     .add(R.id.fragment_details_map_lite_mode, childFragment)
                                     .commit()
        }

        childFragment?.getMapAsync(this@DetailsFragment)
    }

    // -- LiveData --

    /**
     * Configures the LiveData thanks to a simple format
     */
    private fun configureRealEstateLiveData() {
        this.mViewModel.getRealEstateWithPhotosById(realEstateId = this.mItemId)
                       .observe(this.viewLifecycleOwner,
                           Observer { this.configureUI(it) }
                       )
    }

    // -- UI --

    /**
     * Configures UI
     * @param realEstateWithPhotos a [RealEstateWithPhotos]
     */
    private fun configureUI(realEstateWithPhotos: RealEstateWithPhotos?) {
        realEstateWithPhotos?.let {
            // Photos
            it.mPhotos?.let { photos ->
                this.mAdapter.updateData(photos)
            }

            // Real estate
            it.mRealEstate?.let { realEstate ->
                // Description
                this.mRootView.fragment_details_description.text =
                    realEstate.mDescription ?:
                            this.getString(R.string.details_no_description)

                // Characteristics
                this.mRootView.fragment_details_surface.text = this.getString(
                    R.string.details_characteristics,
                    realEstate.mSurface ?: 0.0,
                    realEstate.mNumberOfRoom ?: 0
                )

                // Address
                realEstate.mAddress?.let { address ->
                    val fullAddress = """
                        ${address.mStreet ?: this.getString(R.string.details_no_street)}
                        ${address.mCity ?: this.getString(R.string.details_no_city)}
                        ${address.mPostCode ?: this.getString(R.string.details_no_post_code)}
                        ${address.mCountry ?: this.getString(R.string.details_no_country)}
                    """.trimIndent()

                    this.mRootView.fragment_details_address.text = fullAddress

                    // Google Maps
                    this.showPointOfInterest(
                        LatLng(
                            address.mLatitude ?: 0.0,
                            address.mLongitude ?: 0.0
                        )
                    )
                }
            }
        }
    }

    // -- Google Maps --

    /**
     * Shows the point of interest into Google Maps
     * @param latLng a [LatLng] that contains the location
     */
    private fun showPointOfInterest(latLng: LatLng) {
        this.mGoogleMap?.let {
            it.clear()
            it.addMarker(
                MarkerOptions().position(latLng)
                               .title(this.getString(R.string.title_marker))
            )

            it.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        }
    }
}