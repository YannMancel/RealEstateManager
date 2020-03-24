package com.mancel.yann.realestatemanager.views.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.annotation.LayoutRes
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
 * A [BaseFragment] subclass which implements [AdapterListener] and [DialogListener].
 */
class CreatorFragment : BaseFragment(), AdapterListener, DialogListener {

    // FIELDS --------------------------------------------------------------------------------------

    private lateinit var mAdapter: PhotoAdapter
    private lateinit var mLiveData: PhotoCreatorLiveData

    companion object {
        const val REQUEST_CODE_PHOTO = 100
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- BaseFragment --

    @LayoutRes
    override fun getFragmentLayout(): Int = R.layout.fragment_creator

    override fun configureDesign() {
        // UI
        this.configureListenerOFEachButton()
        this.configureRecyclerView()

        // LiveData
        this.configurePhotoCreatorLiveData()
    }

    override fun actionAfterPermission() = this.actionToAddPhoto()

    // -- Fragment --

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            // To handle the photo
            REQUEST_CODE_PHOTO -> {
                if (resultCode == RESULT_OK) {
                    data?.let {
                        this.handlePhoto(it.data!!)
                    }
                }
                else {
                    Log.d(this::class.simpleName, "PHOTO CANCELED")
                }
            }

            else -> { /* Ignore all other requests */}
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
                                   .show(this.activity!!.supportFragmentManager, "DIALOG PHOTO")
            }

            else -> { /* Ignore all ids */}
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

    // -- Listeners --

    /**
     * Configures the listener of Each button
     */
    private fun configureListenerOFEachButton() {
        // Button: Add photo
        this.mRootView.fragment_creator_add_photo.setOnClickListener {
            this.actionToAddPhoto()
        }

        // FAB
        this.mRootView.fragment_creator_fab.setOnClickListener {
            Log.d(this@CreatorFragment::class.simpleName, "FAB")
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
        val viewManager = LinearLayoutManager(this.context,
                                              LinearLayoutManager.HORIZONTAL,
                                             false)

        // Divider
        val divider = DividerItemDecoration(this.context,
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

    // -- Photo --

    /**
     * Action to add a photo
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
     * @param uri a [Uri] that corresponds to the path of photo from external storage
     */
    private fun handlePhoto(uri: Uri) {
        // todo 24/03/2020 Analyse if possible with PhotoCreatorLiveData & database (see URL)

        PhotoDialogFragment.newInstance(callback = this@CreatorFragment, uri = uri)
                           .show(this.activity!!.supportFragmentManager, "DIALOG PHOTO")
    }
}