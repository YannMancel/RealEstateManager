package com.mancel.yann.realestatemanager.views.fragments

import com.mancel.yann.realestatemanager.R
import com.mancel.yann.realestatemanager.views.bases.BaseFragment

/**
 * Created by Yann MANCEL on 20/02/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.views.fragments
 *
 * A [BaseFragment] subclass.
 */
class DetailsFragment : BaseFragment() {

    // METHODS -------------------------------------------------------------------------------------

    // -- BaseFragment --

    override fun getFragmentLayout(): Int = R.layout.fragment_details

    override fun configureDesign() {}
}