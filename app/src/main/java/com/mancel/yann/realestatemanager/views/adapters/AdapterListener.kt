package com.mancel.yann.realestatemanager.views.adapters

import android.view.View

/**
 * Created by Yann MANCEL on 24/02/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.views.adapters
 */
interface AdapterListener {

    // METHODS -------------------------------------------------------------------------------------

    /**
     * Called with the updateData method of adapter
     */
    fun onDataChanged()

    /**
     * Called when a view has been clicked.
     * @param v The [View] that was clicked.
     */
    fun onClick(v: View?)
}