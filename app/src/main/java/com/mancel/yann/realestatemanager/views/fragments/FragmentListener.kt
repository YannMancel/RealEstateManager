package com.mancel.yann.realestatemanager.views.fragments

import android.view.View

/**
 * Created by Yann MANCEL on 24/02/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.views.fragments
 */
interface FragmentListener {

    // METHODS -------------------------------------------------------------------------------------

    /**
     * Called when the user has selected an item of [ListFragment]
     * @param v The [View] that was clicked.
     */
    fun navigateToDetailsFragment(v: View?)
}