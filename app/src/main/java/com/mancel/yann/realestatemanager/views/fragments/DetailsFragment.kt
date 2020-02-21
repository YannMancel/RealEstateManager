package com.mancel.yann.realestatemanager.views.fragments

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mancel.yann.realestatemanager.R
import com.mancel.yann.realestatemanager.views.adapters.PhotoAdapter
import com.mancel.yann.realestatemanager.views.bases.BaseFragment
import kotlinx.android.synthetic.main.fragment_details.view.*

/**
 * Created by Yann MANCEL on 20/02/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.views.fragments
 *
 * A [BaseFragment] subclass.
 */
class DetailsFragment : BaseFragment() {

    // FIELDS --------------------------------------------------------------------------------------

    private lateinit var mAdapter: PhotoAdapter

    // METHODS -------------------------------------------------------------------------------------

    // -- BaseFragment --

    override fun getFragmentLayout(): Int = R.layout.fragment_details

    override fun configureDesign() {
        // UI
        this.configureRecyclerView()

        // Test
        this.mAdapter.apply {
            updateData(listOf("Room", "Bedroom", "Bathroom", "Bed"))
        }
    }

    // -- RecyclerView --

    /**
     * Configures the [RecyclerView]
     */
    private fun configureRecyclerView() {
        // Adapter
        this.mAdapter = PhotoAdapter()

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