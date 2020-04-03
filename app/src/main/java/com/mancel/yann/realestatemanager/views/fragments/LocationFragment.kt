package com.mancel.yann.realestatemanager.views.fragments

import androidx.annotation.LayoutRes
import com.mancel.yann.realestatemanager.R

/**
 * Created by Yann MANCEL on 20/02/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.views.fragments
 *
 * A [BaseFragment] subclass.
 */
class LocationFragment : BaseFragment() {

    // METHODS -------------------------------------------------------------------------------------

    // -- BaseFragment --

    @LayoutRes
    override fun getFragmentLayout(): Int = R.layout.fragment_location

    override fun configureDesign() {}
}