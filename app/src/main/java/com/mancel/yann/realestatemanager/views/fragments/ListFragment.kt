package com.mancel.yann.realestatemanager.views.fragments

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mancel.yann.realestatemanager.R
import com.mancel.yann.realestatemanager.views.adapters.AdapterListener
import com.mancel.yann.realestatemanager.views.adapters.RealEstateAdapter
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

    override fun configureDesign(savedInstanceState: Bundle?) {
        // UI
        this.configureRecyclerView()

        // LiveData
        this.configureRealEstateLiveData()
    }

    // -- AdapterListener interface --

    override fun onDataChanged() {
        this.mRootView.fragment_list_no_data.visibility =
            if (this.mAdapter.itemCount == 0)
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
     * Configures the RecyclerView
     */
    private fun configureRecyclerView() {
        // Adapter
        this.mAdapter = RealEstateAdapter(
            mCallback = this@ListFragment
        )

        // LayoutManager
        val viewManager = LinearLayoutManager(
            this.requireContext()
        )

        // Divider
        val divider = DividerItemDecoration(
            this.requireContext(),
            DividerItemDecoration.VERTICAL
        )

        // RecyclerView
        with(this.mRootView.fragment_list_RecyclerView) {
            setHasFixedSize(true)
            layoutManager = viewManager
            addItemDecoration(divider)
            adapter = this@ListFragment.mAdapter
        }
    }

    // -- LiveData --

    /**
     * Configures the LiveData thanks to a simple format
     */
    private fun configureRealEstateLiveData() {
        // todo - 06/04/2020 - Next feature: Add user's authentication instead of 1L
        this.mViewModel
            .getRealEstatesWithPhotosByUserId(userId = 1L)
            .observe(
                this.viewLifecycleOwner,
                Observer { this.mAdapter.updateData(it) }
            )
    }
}