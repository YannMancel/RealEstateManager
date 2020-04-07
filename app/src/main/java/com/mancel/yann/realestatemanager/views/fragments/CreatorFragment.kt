package com.mancel.yann.realestatemanager.views.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.annotation.LayoutRes
import androidx.lifecycle.LiveData
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
import com.mancel.yann.realestatemanager.models.Address
import com.mancel.yann.realestatemanager.models.Photo
import com.mancel.yann.realestatemanager.models.RealEstate
import com.mancel.yann.realestatemanager.views.adapters.AdapterListener
import com.mancel.yann.realestatemanager.views.adapters.PhotoAdapter
import com.mancel.yann.realestatemanager.views.dialogs.DialogListener
import com.mancel.yann.realestatemanager.views.dialogs.PhotoDialogFragment
import kotlinx.android.synthetic.main.fragment_creator.*
import kotlinx.android.synthetic.main.fragment_creator.view.*
import java.text.SimpleDateFormat

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
    private lateinit var mPhotosFromDatabase: LiveData<List<Photo>>
    private lateinit var mPhotoCreatorLiveData: PhotoCreatorLiveData
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
        this.configurePhotosFomDatabase()
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
            R.id.item_photo_delete_media -> this.mPhotoCreatorLiveData.removePhoto(v.tag as Photo)

            // Button: EDIT
            R.id.item_photo_edit_media -> {
                PhotoDialogFragment.newInstance(
                                        callback = this@CreatorFragment,
                                        urlPhoto = (v.tag as Photo).mUrlPicture,
                                        description = (v.tag as Photo).mDescription,
                                        mode = PhotoDialogFragment.PhotoDialogMode.UPDATE)
                                   .show(this.requireActivity().supportFragmentManager, "DIALOG PHOTO")
            }

            else -> { /* Ignore all ids */ }
        }
    }

    // -- DialogListener interface --

    override fun getSelectedPhotoFromDialog(
        photo: Photo,
        mode: PhotoDialogFragment.PhotoDialogMode
    ) {
        // Changes the real estate id
        photo.apply {
            // Test
            mRealEstateId = 1L
        }

        when (mode) {
            // ADD
            PhotoDialogFragment.PhotoDialogMode.ADD -> this.mPhotoCreatorLiveData.addPhoto(photo)

            // UPDATE
            PhotoDialogFragment.PhotoDialogMode.UPDATE -> this.mPhotoCreatorLiveData.updatePhoto(photo)
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
        this.configureListenerOfFields(
            this.mRootView.fragment_creator_type,
            this.mRootView.fragment_creator_price,
            this.mRootView.fragment_creator_surface,
            this.mRootView.fragment_creator_number_of_room,
            this.mRootView.fragment_creator_description,
            this.mRootView.fragment_creator_effective_date
        )

        // Hides field for address
        this.mRootView.fragment_creator_address.visibility = View.GONE
        this.mRootView.fragment_creator_city.visibility = View.GONE
        this.mRootView.fragment_creator_post_code.visibility = View.GONE
        this.mRootView.fragment_creator_country.visibility = View.GONE

        // Type: Populates the adapter
        (this.mRootView.fragment_creator_type.editText as? AutoCompleteTextView)?.setAdapter(
            ArrayAdapter(
                this.requireContext(),
                R.layout.item_type,
                this.resources.getStringArray(R.array.creator_types)
            )
        )
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
        // Show field for address
        this.mRootView.fragment_creator_address.visibility = View.VISIBLE
        this.mRootView.fragment_creator_city.visibility = View.VISIBLE
        this.mRootView.fragment_creator_post_code.visibility = View.VISIBLE
        this.mRootView.fragment_creator_country.visibility = View.VISIBLE

        // Show Google Maps
        this@CreatorFragment.childFragmentManager.fragments[0].view?.visibility = View.VISIBLE

        // Retrieve address
        var streetNumber: String? = null
        var route: String? = null
        var locality: String? = null
        var postalCode: String? = null
        var country: String? = null

        place.addressComponents?.asList()?.forEach {
            when (it.types[0]) {
                "street_number"-> streetNumber = it.name
                "route"-> route = it.name
                "locality"-> locality = it.name
                "postal_code"-> postalCode = it.name
                "country"-> country = it.name

                else -> { /* Ignore all other types */ }
            }
        }

        // Street
        this.mRootView.fragment_creator_address.editText?.text?.let {
            it.clear()
            it.append("$streetNumber $route")
        }

        // City
        this.mRootView.fragment_creator_city.editText?.text?.let {
            it.clear()
            it.append(locality)
        }

        // Post code
        this.mRootView.fragment_creator_post_code.editText?.text?.let {
            it.clear()
            it.append(postalCode)
        }

        // Country
        this.mRootView.fragment_creator_country.editText?.text?.let {
            it.clear()
            it.append(country)
        }
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
        with(this.mRootView.fragment_creator_RecyclerView) {
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
     * Configures all [Photo] from database
     */
    private fun configurePhotosFomDatabase() {
        this.mPhotosFromDatabase =  this.mViewModel.getPhotos().apply {
            observe(this@CreatorFragment.viewLifecycleOwner, Observer {
                /* Do nothing */
            })
        }
    }

    /**
     * Configures the [PhotoCreatorLiveData]
     */
    private fun configurePhotoCreatorLiveData() {
        this.mPhotoCreatorLiveData =  this.mViewModel.getPhotoCreator().apply {
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
                this.mCallback?.showMessage(
                    this.getString(R.string.creator_search_cancel)
                )
            }
        }
    }

    // -- Photo --

    /**
     * Action to add a [Photo]
     */
    private fun actionToAddPhoto() {
        if (this.checkReadExternalStoragePermission()) {
            // Goal: Retrieves a photo from external storage
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
            data?.data?.let { uri ->
                // Database: Search if is already present into the database
                val isAlreadyPresentIntoDatabase = this.mPhotosFromDatabase.value?.any {
                    it.mUrlPicture == uri.toString()
                } ?: false

                // CreatorLiveData: Search if is already present into the LiveData
                val isAlreadyPresentIntoCreator = this.mPhotoCreatorLiveData.value?.any {
                    it.mUrlPicture == uri.toString()
                } ?: false

                if (!isAlreadyPresentIntoDatabase && !isAlreadyPresentIntoCreator) {
                    PhotoDialogFragment.newInstance(
                                            callback = this@CreatorFragment,
                                            urlPhoto = uri.toString())
                                       .show(
                                           this.requireActivity().supportFragmentManager,
                                           "DIALOG PHOTO"
                                       )
                }
                else {
                    this.mCallback?.showMessage(
                        this.getString(R.string.creation_photo_already_present)
                    )
                }
            }
        }
        else {
            this.mCallback?.showMessage(
                this.getString(R.string.creation_photo_cancel)
            )
        }
    }

    // -- Real Estate --

    /**
     * Action to add a [RealEstate]
     */
    private fun actionToAddRealEstate() {
        // Errors
        val isCanceled = this.configureErrorOfFields(
            this.mRootView.fragment_creator_type,
            this.mRootView.fragment_creator_price,
            this.mRootView.fragment_creator_surface,
            this.mRootView.fragment_creator_number_of_room,
            this.mRootView.fragment_creator_description,
            this.mRootView.fragment_creator_effective_date
        )

        if (isCanceled) {
            this.mCallback?.showMessage(
                this.getString(R.string.creator_real_estate_lack_information)
            )
            return
        }

        // todo - 06/04/2020 - Add test on address

        // todo - 06/04/2020 - Next feature: Add user's authentication instead of 1L
        val realEstate = RealEstate(
            mType = this.fragment_creator_type.editText?.text?.toString(),
            mPrice = this.fragment_creator_price.editText?.text?.toString()?.toDouble(),
            mSurface = this.mRootView.fragment_creator_surface.editText?.text?.toString()?.toDouble(),
            mNumberOfRoom = this.mRootView.fragment_creator_number_of_room.editText?.text?.toString()?.toInt(),
            mDescription = this.mRootView.fragment_creator_description.editText?.text?.toString(),
            mIsEnable = this.mRootView.fragment_creator_enable.isChecked,
            mEffectiveDate = SimpleDateFormat("dd/MM/yyyy").parse(this.mRootView.fragment_creator_effective_date.editText?.text?.toString()),
            mSaleDate = null,
            mEstateAgentId = 1L,
            mAddress = Address(
                mStreet = this.fragment_creator_address.editText?.text?.toString(),
                mCity = this.fragment_creator_city.editText?.text?.toString(),
                mPostCode = this.fragment_creator_post_code.editText?.text?.toString()?.toInt(),
                mState = this.fragment_creator_country.editText?.text?.toString()
            )
        )

        this.mViewModel.insertRealEstate(
            realEstate,
            this.mPhotoCreatorLiveData.value,
            null
        )
    }
}