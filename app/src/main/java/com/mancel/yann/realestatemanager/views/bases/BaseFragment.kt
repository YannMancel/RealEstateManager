package com.mancel.yann.realestatemanager.views.bases

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * Created by Yann MANCEL on 20/02/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.views.bases
 *
 * A [Fragment] subclass.
 */
abstract class BaseFragment : Fragment() {

    // METHODS -------------------------------------------------------------------------------------

    /**
     * Gets the integer value of the fragment layout
     * @return an integer that corresponds to the fragment layout
     */
    protected abstract fun getFragmentLayout(): Int

    /**
     * Configures the design of each daughter class
     */
    protected abstract fun configureDesign()

    // -- Fragment --

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(this.getFragmentLayout(), container, false)
    }
}