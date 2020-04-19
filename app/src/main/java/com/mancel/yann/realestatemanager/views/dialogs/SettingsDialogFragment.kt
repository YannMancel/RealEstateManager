package com.mancel.yann.realestatemanager.views.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.switchmaterial.SwitchMaterial
import com.mancel.yann.realestatemanager.R
import com.mancel.yann.realestatemanager.utils.SaveTools
import kotlinx.android.synthetic.main.dialog_fragment_settings.view.*

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

    companion object {
        const val BUNDLE_SWITCH_NOTIFICATION = "BUNDLE_SWITCH_NOTIFICATION"
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- DialogFragment --

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Creates the View thanks to the inflater
        this.mRootView = this.requireActivity().layoutInflater
                                               .inflate(R.layout.dialog_fragment_settings, null)

        this.fetchDataFromSharedPreferences()
        this.configureSwitch()

        return MaterialAlertDialogBuilder(this.requireContext()).setView(this.mRootView)
                                                                .setTitle(R.string.title_settings_dialog_fragment)
                                                                .create()
    }

    // -- SharedPreferences --

    /**
     * Fetches data from the SharedPreferences
     */
    private fun fetchDataFromSharedPreferences() {
        this.mRootView.dialog_fragment_settings_switch.isChecked =
            SaveTools.fetchBooleanFromSharedPreferences(
                this.requireContext(),
                BUNDLE_SWITCH_NOTIFICATION
            )
    }

    // -- Switch --

    /**
     * Configures the [SwitchMaterial]
     */
    private fun configureSwitch() {
        this.mRootView.dialog_fragment_settings_switch.setOnClickListener {
            if (it is SwitchMaterial) {
                SaveTools.saveBooleanIntoSharedPreferences(
                    this.requireContext(),
                    BUNDLE_SWITCH_NOTIFICATION,
                    it.isChecked
                )
            }
        }
    }
}