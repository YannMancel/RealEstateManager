package com.mancel.yann.realestatemanager.views.fragments

import android.util.Log
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mancel.yann.realestatemanager.R
import com.mancel.yann.realestatemanager.views.adapters.AdapterListener
import com.mancel.yann.realestatemanager.views.adapters.PhotoAdapter
import com.mancel.yann.realestatemanager.views.bases.BaseFragment
import kotlinx.android.synthetic.main.fragment_details.view.*

/**
 * Created by Yann MANCEL on 20/02/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.views.fragments
 *
 * A [BaseFragment] subclass which implements [AdapterListener].
 */
class DetailsFragment : BaseFragment(), AdapterListener {

    // FIELDS --------------------------------------------------------------------------------------

    private lateinit var mAdapter: PhotoAdapter

    // METHODS -------------------------------------------------------------------------------------

    // -- BaseFragment --

    @LayoutRes
    override fun getFragmentLayout(): Int = R.layout.fragment_details

    override fun configureDesign() {
        // UI
        this.configureRecyclerView()

        // Test
        this.mAdapter.apply {
            updateData(listOf("Room", "Bedroom", "Bathroom", "Bed"))
        }
    }

    // -- AdapterListener interface --

    override fun onDataChanged() {
        this.mRootView.fragment_details_no_data.visibility = if (this.mAdapter.itemCount == 0)
                                                                 View.VISIBLE
                                                             else
                                                                 View.GONE
    }

    override fun onClick(v: View?) {
        Log.d(this::class.java.simpleName, "Data: ${v?.tag as? String}")
    }

    // -- RecyclerView --

    /**
     * Configures the [RecyclerView]
     */
    private fun configureRecyclerView() {
        // Adapter
        this.mAdapter = PhotoAdapter(mCallback = this)

        // LayoutManager
        val viewManager = LinearLayoutManager(this.context,
                                              LinearLayoutManager.HORIZONTAL,
                                             false)

        // Divider
        val divider = DividerItemDecoration(this.context,
                                            DividerItemDecoration.HORIZONTAL)

        // RecyclerView
        this.mRootView.fragment_details_RecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            addItemDecoration(divider)
            adapter = mAdapter
        }
    }
}