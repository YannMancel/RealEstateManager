package com.mancel.yann.realestatemanager.views.dialogs

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.mancel.yann.realestatemanager.R
import kotlinx.android.synthetic.main.dialog_selected_photo.view.*

/**
 * Created by Yann MANCEL on 20/03/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.views.dialogs
 *
 * A [DialogFragment] subclass.
 */
class PhotoDialogFragment : DialogFragment() {

    // FIELDS --------------------------------------------------------------------------------------

    private lateinit var mRootView: View
    private val mUriPhoto: Uri by lazy { this.arguments!!.getParcelable(BUNDLE_KEY_URI) as Uri}

    // METHODS -------------------------------------------------------------------------------------

    companion object {

        const val BUNDLE_KEY_URI = "BUNDLE_KEY_URI"

        /**
         * Gets a new instance of [PhotoDialogFragment]
         * @param uri a [Uri] that corresponds to the path of photo from external storage
         */
        fun newInstance(uri: Uri): PhotoDialogFragment {
            val dialog = PhotoDialogFragment()

            // Bundle into Argument of Fragment
            dialog.arguments = Bundle().apply {
                putParcelable(BUNDLE_KEY_URI, uri)
            }

            return dialog
        }
    }

    // -- DialogFragment --

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

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

    /**
     * Configures the buttons
     */
    private fun configureButtons() {
        this.mRootView.dialog_selected_photo_yes.setOnClickListener {
            // No description
            if (this.mRootView.dialog_selected_photo_description.editText?.text.toString().isEmpty()) {
                this.mRootView.dialog_selected_photo_description.error = this.getString(R.string.no_description)
                return@setOnClickListener
            }

            Log.d(this@PhotoDialogFragment::class.simpleName, "BUTTON YES: GOOD")
        }

        this.mRootView.dialog_selected_photo_no.setOnClickListener {
            this.dismiss()
        }
    }
}