package com.mancel.yann.realestatemanager.views.fragments

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mancel.yann.realestatemanager.R
import com.mancel.yann.realestatemanager.views.adapters.AdapterListener
import com.mancel.yann.realestatemanager.views.adapters.RealEstateAdapter
import com.mancel.yann.realestatemanager.views.bases.BaseFragment
import kotlinx.android.synthetic.main.fragment_list.view.*

/**
 * Created by Yann MANCEL on 20/02/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.views.fragments
 *
 * A [BaseFragment] subclass which implements [AdapterListener].
 */
class ListFragment : BaseFragment(), AdapterListener {

    // FIELDS --------------------------------------------------------------------------------------

    private lateinit var mAdapter: RealEstateAdapter

    // METHODS -------------------------------------------------------------------------------------

    // -- BaseFragment --

    @LayoutRes
    override fun getFragmentLayout(): Int = R.layout.fragment_list

    override fun configureDesign() {
        // UI
        this.configureRecyclerView()

        // Test
        this.mAdapter.apply {
            updateData(listOf("Yann", "Mel", "Logan", "Nata"))
        }
    }

    // -- AdapterListener interface --

    override fun onDataChanged() {
        this.mRootView.fragment_list_no_data.visibility = if (this.mAdapter.itemCount == 0)
                                                              View.VISIBLE
                                                          else
                                                              View.GONE
    }

    override fun onClick(v: View?) {
        // Callback from Fragment to Activity
        this.mCallback?.navigateToDetailsFragment(v)
    }

    // -- RecyclerView --

    /**
     * Configures the [RecyclerView]
     */
    private fun configureRecyclerView() {
        // Adapter
        this.mAdapter = RealEstateAdapter(mCallback = this@ListFragment)

        // LayoutManager
        val viewManager = LinearLayoutManager(this.context)

        // Divider
        val divider = DividerItemDecoration(this.context,
                                            DividerItemDecoration.VERTICAL)

        // RecyclerView
        this.mRootView.fragment_list_RecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            addItemDecoration(divider)
            adapter = mAdapter
        }
    }
}