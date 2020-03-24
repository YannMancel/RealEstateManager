package com.mancel.yann.realestatemanager.views.dialogs

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.mancel.yann.realestatemanager.R
import com.mancel.yann.realestatemanager.models.Photo
import kotlinx.android.synthetic.main.dialog_selected_photo.view.*
import java.lang.ref.WeakReference

/**
 * Created by Yann MANCEL on 20/03/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.views.dialogs
 *
 * A [DialogFragment] subclass.
 */
class PhotoDialogFragment : DialogFragment() {

    // ENUMS ---------------------------------------------------------------------------------------

    enum class PhotoDialogMode {ADD, UPDATE}
    private var mMode: PhotoDialogMode = PhotoDialogMode.ADD

    // FIELDS --------------------------------------------------------------------------------------

    private val mUriPhoto: Uri? by lazy { this.arguments!!.getParcelable(BUNDLE_KEY_URI) as? Uri}
    private val mDescription: String? by lazy { this.arguments!!.getString(BUNDLE_KEY_DESCRIPTION)}
    private lateinit var mRootView: View
    private var mCallback: WeakReference<DialogListener?>? = null

    // METHODS -------------------------------------------------------------------------------------

    companion object {

        const val BUNDLE_KEY_URI = "BUNDLE_KEY_URI"
        const val BUNDLE_KEY_DESCRIPTION = "BUNDLE_KEY_DESCRIPTION"

        /**
         * Gets a new instance of [PhotoDialogFragment]
         * @param callback  a [DialogListener]
         * @param uri       an [Uri] that corresponds to the path of photo from external storage
         * @param mode      a [PhotoDialogMode]
         */
        fun newInstance(callback: DialogListener,
                        uri: Uri?,
                        description: String? = null,
                        mode: PhotoDialogMode = PhotoDialogMode.ADD): PhotoDialogFragment {
            val dialog = PhotoDialogFragment().apply {
                setCallback(callback)
                setMode(mode)
            }

            // Bundle into Argument of Fragment
            dialog.arguments = Bundle().apply {
                putParcelable(BUNDLE_KEY_URI, uri)
                putString(BUNDLE_KEY_DESCRIPTION, description)
            }

            return dialog
        }
    }

    // -- DialogFragment --

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Creates the View thanks to the inflater
        this.mRootView = this.activity!!.layoutInflater
                                        .inflate(R.layout.dialog_selected_photo, null)

        this.configureDisplayingOfPhoto()
        this.configureDescriptionOfPhoto()
        this.configureButtons()

        return AlertDialog.Builder(this.activity!!)
                          .setView(this.mRootView)
                          .setTitle(R.string.title_photo_dialog_fragment)
                          .create()
    }

    // -- Callback --

    /**
     * Sets the callback into a [WeakReference] of [DialogListener]
     * @param callback a [DialogListener]
     */
    private fun setCallback(callback: DialogListener) {
        this.mCallback = WeakReference(callback)
    }

    // -- Mode --

    /**
     * Sets the mode into a [PhotoDialogMode]
     * @param mode a [PhotoDialogMode]
     */
    private fun setMode(mode: PhotoDialogMode) {
        this.mMode = mode
    }

    // -- Photo --

    /**
     * Configures the displaying of the photo
     */
    private fun configureDisplayingOfPhoto() {
        // Image with Glide library
        Glide.with(this.activity!!)
             .load(this.mUriPhoto)
             .centerCrop()
             .fallback(R.drawable.ic_photo)
             .error(R.drawable.ic_clear)
             .into(this.mRootView.dialog_selected_photo_image)
    }

    /**
     * Configures the description of the photo
     */
    private fun configureDescriptionOfPhoto() {
        // Description from argument
        if (this.mDescription != null) {
            this.mRootView.dialog_selected_photo_description.editText?.text?.append(this.mDescription)
        }

        // Add listener
        this.mRootView.dialog_selected_photo_description.editText?.addTextChangedListener(
            object : TextWatcher {

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // Do nothing
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // Reset error
                    mRootView.dialog_selected_photo_description.error = null
                }

                override fun afterTextChanged(s: Editable?) {
                    // Do nothing
                }
            })
    }

    // -- Button --

    /**
     * Configures the buttons
     */
    private fun configureButtons() {
        // Button: YES
        this.mRootView.dialog_selected_photo_yes.setOnClickListener {
            this.actionOfYesButton()
        }

        // Button: NO
        this.mRootView.dialog_selected_photo_no.setOnClickListener {
            // Close Dialog
            this.dismiss()
        }
    }

    /**
     * Action for the Yes button
     */
    private fun actionOfYesButton() {
        // No description
        if (this.mRootView.dialog_selected_photo_description.editText?.text.toString().isEmpty()) {
            this.mRootView.dialog_selected_photo_description.error = this.getString(R.string.no_description)
            return
        }

        // Photo
        val photo = Photo(mUrlPicture = this.mUriPhoto.toString(),
                          mDescription = this.mRootView.dialog_selected_photo_description.editText!!.text.toString())

        // Callback
        this.mCallback?.get()?.getSelectedPhotoFromDialog(photo, this.mMode)

        // Close Dialog
        this.dismiss()
    }
}