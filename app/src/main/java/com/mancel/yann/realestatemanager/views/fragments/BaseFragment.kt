package com.mancel.yann.realestatemanager.views.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.mancel.yann.realestatemanager.viewModels.RealEstateViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.ClassCastException

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
            // To access to external storage
            REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    this.actionAfterPermission()
                }
                else {
                    Log.d(this::class.simpleName, "PERMISSION_DENIED")
                }
            }

            else -> { /* Ignore all other requests */}
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
     * Method to override to perform action after the granted permission
     */
    protected open fun actionAfterPermission() {/* Do nothing here */}
}