package com.mancel.yann.realestatemanager.views.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.ResolvableApiException
import com.mancel.yann.realestatemanager.R
import com.mancel.yann.realestatemanager.viewModels.RealEstateViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Yann MANCEL on 20/02/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.views.bases
 *
 * A [Fragment] subclass.
 */
abstract class BaseFragment : Fragment() {

    // FIELDS --------------------------------------------------------------------------------------

    protected lateinit var mRootView: View
    protected var mCallback: FragmentListener? = null

    protected val mViewModel: RealEstateViewModel by viewModel()

    companion object {
        const val REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE = 1000
        const val REQUEST_CODE_PERMISSION_ACCESS_FINE_LOCATION = 2000
        const val REQUEST_CODE_CHECK_SETTINGS_TO_LOCATION = 3000
    }

    // METHODS -------------------------------------------------------------------------------------

    /**
     * Gets the integer value of the fragment layout
     * @return an integer that corresponds to the fragment layout
     */
    @LayoutRes
    protected abstract fun getFragmentLayout(): Int

    /**
     * Configures the design of each daughter class
     */
    protected abstract fun configureDesign()

    // -- Fragment --

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Configures the callback to the parent activity
        if (context is FragmentListener) {
            this.mCallback = context
        }
        else {
            throw ClassCastException("$context must implement FragmentListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        this.mRootView = inflater.inflate(this.getFragmentLayout(), container, false)

        // Configures the design
        this.configureDesign()

        return this.mRootView
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            // Access to the external storage or the current location of device
            REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE,
            REQUEST_CODE_PERMISSION_ACCESS_FINE_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    this.actionAfterPermission()
                }
                else {
                    this.mCallback?.showMessage(this.getString(R.string.permission_denied))
                }
            }

            else -> { /* Ignore all other requests */}
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Check settings to location
        if (requestCode == REQUEST_CODE_CHECK_SETTINGS_TO_LOCATION && resultCode == RESULT_OK) {
            this.actionAfterPermission()
        }
    }

    // -- Permission --

    /**
     * Checks the permission: READ_EXTERNAL_STORAGE
     */
    protected fun checkReadExternalStoragePermission(): Boolean {
        val permissionResult = ContextCompat.checkSelfPermission(this.requireContext(),
                                                                 Manifest.permission.READ_EXTERNAL_STORAGE)

        return when (permissionResult) {
            PackageManager.PERMISSION_GRANTED -> true

            else -> {
                this.requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE
                )

                false
            }
        }
    }

    /**
     * Checks the permission: ACCESS_FINE_LOCATION
     */
    private fun checkAccessFineLocationPermission(): Boolean {
        val permissionResult = ContextCompat.checkSelfPermission(this.requireContext(),
                                                                 Manifest.permission.ACCESS_FINE_LOCATION)

        return when (permissionResult) {
            PackageManager.PERMISSION_GRANTED -> true

            else -> {
                this.requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_CODE_PERMISSION_ACCESS_FINE_LOCATION
                )

                false
            }
        }
    }

    /**
     * Method to override to perform action after the granted permission
     */
    protected open fun actionAfterPermission() {/* Do nothing here */}

    // -- Exceptions --

    /**
     * Handles the location [Exception]
     * @param exception an [Exception]
     * @return a boolean that is true if there is an [Exception]
     */
    protected fun handleLocationException(exception: Exception?): Boolean {
        // No exception
        if (exception == null) {
            return false
        }

        when (exception) {
            is SecurityException -> this.checkAccessFineLocationPermission()

            is ResolvableApiException -> {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    exception.startResolutionForResult(
                        this.requireActivity(),
                        REQUEST_CODE_CHECK_SETTINGS_TO_LOCATION
                    )
                }
                catch (sendEx: SendIntentException) {
                    // Ignore the error.
                }
            }
        }

        return true
    }
}