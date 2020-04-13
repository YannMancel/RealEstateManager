package com.mancel.yann.realestatemanager.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

/**
 * Created by Yann MANCEL on 05/04/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.utils
 */
object MessageTools {

    /**
     * Shows a [Snackbar] with a message
     * @param view      a [View] that will display the message
     * @param message   a [String] that contains the message to display
     */
    fun showMessageWithSnackbar(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
                .show()
    }
}