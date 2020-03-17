package com.mancel.yann.realestatemanager.views.bases

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.mancel.yann.realestatemanager.viewModels.RealEstateViewModel
import com.mancel.yann.realestatemanager.views.fragments.FragmentListener
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

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        this.mRootView = inflater.inflate(this.getFragmentLayout(), container, false)

        // Configures the design
        this.configureDesign()

        return this.mRootView
    }
}