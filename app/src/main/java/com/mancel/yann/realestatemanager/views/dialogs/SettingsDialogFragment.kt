package com.mancel.yann.realestatemanager.views.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mancel.yann.realestatemanager.R

/**
 * Created by Yann MANCEL on 10/03/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.views.dialogs
 *
 * A [DialogFragment] subclass.
 */
class SettingsDialogFragment : DialogFragment() {

    // FIELDS --------------------------------------------------------------------------------------

    private lateinit var mRootView: View

    // METHODS -------------------------------------------------------------------------------------

    // -- DialogFragment --

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Creates the View thanks to the inflater
        this.mRootView = this.requireActivity().layoutInflater
                                               .inflate(R.layout.dialog_fragment_settings, null)

        return MaterialAlertDialogBuilder(this.requireContext()).setView(this.mRootView)
                                                                .setTitle(R.string.title_settings_dialog_fragment)
                                                                .create()
    }
}