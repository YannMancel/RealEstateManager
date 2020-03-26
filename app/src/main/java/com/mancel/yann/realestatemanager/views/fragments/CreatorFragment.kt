package com.mancel.yann.realestatemanager.views.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.annotation.LayoutRes
import androidx.core.view.isVisible
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
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.textfield.TextInputLayout
import com.mancel.yann.realestatemanager.R
import com.mancel.yann.realestatemanager.liveDatas.PhotoCreatorLiveData
import com.mancel.yann.realestatemanager.models.Photo
import com.mancel.yann.realestatemanager.views.adapters.AdapterListener
import com.mancel.yann.realestatemanager.views.adapters.PhotoAdapter
import com.mancel.yann.realestatemanager.views.bases.BaseFragment
import com.mancel.yann.realestatemanager.views.dialogs.DialogListener
import com.mancel.yann.realestatemanager.views.dialogs.PhotoDialogFragment
import kotlinx.android.synthetic.main.fragment_creator.view.*

/**
 * Created by Yann MANCEL on 20/02/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.views.fragments
 *
 * A [BaseFragment] subclass which implements [AdapterListener], [DialogListener]
 * and [OnMapReadyCallback].
 */
class CreatorFragment : BaseFragment(), AdapterListener, DialogListener, OnMapReadyCallback {

    // FIELDS --------------------------------------------------------------------------------------

    private lateinit var mAdapter: PhotoAdapter
    private lateinit var mLiveData: PhotoCreatorLiveData
    private var mGoogleMap: GoogleMap? = null

    companion object {
        const val REQUEST_CODE_PHOTO = 100
        const val REQUEST_CODE_AUTOCOMPLETE = 200
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- BaseFragment --

    @LayoutRes
    override fun getFragmentLayout(): Int = R.layout.fragment_creator

    override fun configureDesign() {
        // UI
        this.configureFieldsOfData()
        this.configureListenerOFEachButton()
        this.configureRecyclerView()
        this.configureSupportMapFragment()

        // LiveData
        this.configurePhotoCreatorLiveData()
    }

    override fun actionAfterPermission() = this.actionToAddPhoto()

    // -- Fragment --

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            // Photo
            REQUEST_CODE_PHOTO -> this.handlePhoto(resultCode, data)

            // Search
            REQUEST_CODE_AUTOCOMPLETE -> this.handleAddress(resultCode, data)

            else -> { /* Ignore all other requests */ }
        }
    }

    // -- AdapterListener interface --

    override fun onDataChanged() {
        this.mRootView.fragment_creator_RecyclerView.visibility = if (this.mAdapter.itemCount != 0)
                                                                      View.VISIBLE
                                                                  else
                                                                      View.GONE
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            // Button: DELETE
            R.id.item_photo_delete_media -> this.mLiveData.removePhoto(v.tag as Photo)

            // Button: EDIT
            R.id.item_photo_edit_media -> {
                PhotoDialogFragment.newInstance(callback = this@CreatorFragment,
                                                uri = Uri.parse((v.tag as Photo).mUrlPicture),
                                                description = (v.tag as Photo).mDescription,
                                                mode = PhotoDialogFragment.PhotoDialogMode.UPDATE)
                                   .show(this.requireActivity().supportFragmentManager, "DIALOG PHOTO")
            }

            else -> { /* Ignore all ids */ }
        }
    }

    // -- DialogListener interface --

    override fun getSelectedPhotoFromDialog(photo: Photo,
                                            mode: PhotoDialogFragment.PhotoDialogMode) {
        // Changes the real estate id
        photo.apply {
            // Test
            mRealEstateId = 1L
        }

        when (mode) {
            // ADD
            PhotoDialogFragment.PhotoDialogMode.ADD -> this.mLiveData.addPhoto(photo)

            // UPDATE
            PhotoDialogFragment.PhotoDialogMode.UPDATE -> this.mLiveData.updatePhoto(photo)
        }
    }

    // -- OnMapReadyCallback interface --

    override fun onMapReady(googleMap: GoogleMap?) {
        this.mGoogleMap = googleMap

        // TOOLBAR
        this.mGoogleMap?.uiSettings?.isMapToolbarEnabled = false
    }

    // -- Fields of data --

    /**
     * Configures the fields of data
     */
    private fun configureFieldsOfData() {
        this.configureListenerOfFields(this.mRootView.fragment_creator_type,
                                       this.mRootView.fragment_creator_price,
                                       this.mRootView.fragment_creator_address)

        // Hides field for address
        this.mRootView.fragment_creator_address.visibility = View.GONE

        // Type: Populates the adapter
        (this.mRootView.fragment_creator_type.editText as? AutoCompleteTextView)?.setAdapter(
            ArrayAdapter(this.requireContext(),
                         R.layout.item_type,
                         this.resources.getStringArray(R.array.creator_types)))
    }

    /**
     * Configures the listener of each field
     * @param textInputLayouts a variable array of [TextInputLayout]
     */
    private fun configureListenerOfFields(vararg textInputLayouts: TextInputLayout) {
        for (textInputLayout in textInputLayouts) {
            // Add listener
            textInputLayout.editText?.addTextChangedListener(object : TextWatcher {

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // Do nothing
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // After that user has selected an address
                    if (textInputLayout.id == R.id.fragment_creator_address && !textInputLayout.isVisible) {
                        // Address
                        textInputLayout.visibility = View.VISIBLE

                        // Google Maps
                        this@CreatorFragment.childFragmentManager.fragments[0].view?.visibility = View.VISIBLE

                        return
                    }

                    // Reset error
                    textInputLayout.error = null
                }

                override fun afterTextChanged(s: Editable?) {
                    // Do nothing
                }
            })
        }
    }

    /**
     * Configures the error of each field
     * @param textInputLayouts a variable array of [TextInputLayout]
     * @return a [Boolean] with true if is canceled
     */
    private fun configureErrorOfFields(vararg textInputLayouts: TextInputLayout): Boolean {
        var isIncorrect = false

        for (textInputLayout in textInputLayouts) {
            // No Data
            if (textInputLayout.editText?.text.toString().isEmpty()) {
                textInputLayout.error = this.getString(R.string.no_data)
                isIncorrect = true
            }
        }

        return isIncorrect
    }

    /**
     * Shows the address into [TextInputLayout]
     * @param place a [Place] that contains the address
     */
    private fun showAddressIntoField(place: Place) {
        // Shows address
        val addressComponents = place.addressComponents?.asList()

        var streetNumber: String? = null
        var route: String? = null
        var locality: String? = null
        var postalCode: String? = null
        var country: String? = null

        addressComponents?.forEach {
            when (it.types[0]) {
                "street_number"-> streetNumber = it.name
                "route"-> route = it.name
                "locality"-> locality = it.name
                "postal_code"-> postalCode = it.name
                "country"-> country = it.name

                else -> { /* Ignore all other types */ }
            }
        }

        val address = """
                    $streetNumber $route
                    $locality
                    $postalCode
                    $country
                """.trimIndent()

        this.mRootView.fragment_creator_address.editText?.text?.clear()
        this.mRootView.fragment_creator_address.editText?.text?.append(address)
    }

    // -- Listeners --

    /**
     * Configures the listener of Each button
     */
    private fun configureListenerOFEachButton() {
        // Button: Add address
        this.mRootView.fragment_creator_add_address.setOnClickListener {
            this.actionToSearchAddress()
        }

        // Button: Add photo
        this.mRootView.fragment_creator_add_photo.setOnClickListener {
            this.actionToAddPhoto()
        }

        // FAB
        this.mRootView.fragment_creator_fab.setOnClickListener {
            this.actionToAddRealEstate()
        }
    }

    // -- RecyclerView --

    /**
     * Configures the [RecyclerView]
     */
    private fun configureRecyclerView() {
        // Adapter
        this.mAdapter = PhotoAdapter(mCallback = this@CreatorFragment,
                                     mButtonDisplayMode = PhotoAdapter.ButtonDisplayMode.EDIT_MODE)

        // LayoutManager
        val viewManager = LinearLayoutManager(this.requireContext(),
                                              LinearLayoutManager.HORIZONTAL,
                                             false)

        // Divider
        val divider = DividerItemDecoration(this.requireContext(),
                                            DividerItemDecoration.HORIZONTAL)

        // RecyclerView
        this.mRootView.fragment_creator_RecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            addItemDecoration(divider)
            adapter = mAdapter
            visibility = View.GONE
        }
    }

    // -- Child Fragment --

    /**
     * Configures the child fragment which contains the Google Maps
     */
    private fun configureSupportMapFragment() {
        var childFragment = this.childFragmentManager.findFragmentById(R.id.fragment_creator_map_lite_mode) as? SupportMapFragment

        if (childFragment == null) {
            childFragment = SupportMapFragment.newInstance()

            this.childFragmentManager.beginTransaction()
                                     .add(R.id.fragment_creator_map_lite_mode, childFragment)
                                     .commit()
        }

        childFragment?.getMapAsync(this@CreatorFragment)

        // Hides the fragment
        this.childFragmentManager.fragments[0].view?.visibility = View.GONE
    }

    // -- LiveData --

    /**
     * Configures the [PhotoCreatorLiveData]
     */
    private fun configurePhotoCreatorLiveData() {
        this.mLiveData =  this.mViewModel.getPhotoCreator().apply {
            observe(this@CreatorFragment.viewLifecycleOwner, Observer {
                    photos -> this@CreatorFragment.mAdapter.updateData(photos)
            })
        }
    }

    // -- Google Maps --

    /**
     * Shows the point of interest into Google Maps
     * @param latLng a [LatLng] that contains the location
     */
    private fun showPointOfInterest(latLng: LatLng) {
        val newLocation = LatLng(latLng.latitude, latLng.longitude)

        this.mGoogleMap?.clear()
        this.mGoogleMap?.addMarker(MarkerOptions().position(newLocation)
                                                  .title(this.getString(R.string.title_marker)))

        this.mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLng(newLocation))
    }

    /**
     * Action to search the address
     */
    private fun actionToSearchAddress() {
        // Configures Places with the Google Maps key
        Places.initialize(this.requireContext(),
                          this.getString(R.string.google_maps_key))

        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,
                                                listOf(Place.Field.ADDRESS_COMPONENTS,
                                                       Place.Field.LAT_LNG))
                                 .setTypeFilter(TypeFilter.ADDRESS)
                                 .build(this.requireContext())

        this.startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE)
    }

    /**
     * Handles the address
     * @param resultCode    an [Int] that contains the result code
     * @param data          an [Intent] that contains the data
     */
    private fun handleAddress(resultCode: Int, data: Intent?) {
        when (resultCode) {
            RESULT_OK -> {
                // Data
                val place = Autocomplete.getPlaceFromIntent(data!!)

                this.showPointOfInterest(place.latLng!!)
                this.showAddressIntoField(place)
            }

            AutocompleteActivity.RESULT_ERROR -> {
                val status = Autocomplete.getStatusFromIntent(data!!)
                Log.e(this::class.simpleName, "${status.statusMessage} [Place API]")
            }

            else -> {
                Log.d(this::class.simpleName, "SEARCH CANCELED")
            }
        }
    }

    // -- Photo --

    /**
     * Action to add a [Photo]
     */
    private fun actionToAddPhoto() {
        if (this.checkReadExternalStoragePermission()) {
            // Goal: Retrieves a photo from  external storage
            val intent = Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            
            this.startActivityForResult(intent, REQUEST_CODE_PHOTO)
        }
    }

    /**
     * Handles the photo
     * @param resultCode    an [Int] that contains the result code
     * @param data          an [Intent] that contains the data
     */
    private fun handlePhoto(resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            data?.let {
                // todo 24/03/2020 Analyse if possible with PhotoCreatorLiveData & database (see URL)

                PhotoDialogFragment.newInstance(callback = this@CreatorFragment, uri = it.data!!)
                                   .show(this.requireActivity().supportFragmentManager, "DIALOG PHOTO")
            }
        }
        else {
            Log.d(this::class.simpleName, "PHOTO CANCELED")
        }
    }

    // -- Real Estate --

    /**
     * Action to add a [RealEstate]
     */
    private fun actionToAddRealEstate() {
        // Errors
        val isCanceled = this.configureErrorOfFields(this.mRootView.fragment_creator_type,
                                                     this.mRootView.fragment_creator_price)

        if (!isCanceled) {
            // todo 24/03/2020 Add method to create a real estate
        }
    }
}