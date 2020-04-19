package com.mancel.yann.realestatemanager.views.fragments

import android.view.View
import androidx.annotation.LayoutRes
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mancel.yann.realestatemanager.R
import com.mancel.yann.realestatemanager.models.RealEstateWithPhotos
import com.mancel.yann.realestatemanager.views.adapters.AdapterListener
import com.mancel.yann.realestatemanager.views.adapters.RealEstateAdapter
import kotlinx.android.synthetic.main.fragment_search.view.*
import timber.log.Timber

/**
 * Created by Yann MANCEL on 18/04/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.views.fragments
 *
 * A [BaseFragment] subclass which implements [AdapterListener].
 */
class SearchFragment : BaseFragment(), AdapterListener {

    // FIELDS --------------------------------------------------------------------------------------

    private lateinit var mAdapter: RealEstateAdapter
    private var mAllDataFromDatabase: List<RealEstateWithPhotos>? = null

    // METHODS -------------------------------------------------------------------------------------

    // -- BaseFragment --

    @LayoutRes
    override fun getFragmentLayout(): Int = R.layout.fragment_search

    override fun configureDesign() {
        // UI
        this.configureRecyclerView()
        this.configureListenerOfEachComponent()

        // LiveData
        this.configureRealEstateLiveData()
    }

    // -- AdapterListener interface --

    override fun onDataChanged() {
        this.mRootView.fragment_search_no_data.visibility =
            if (this.mAdapter.itemCount == 0)
                View.VISIBLE
            else
                View.GONE
    }

    override fun onClick(v: View?) {
        // Tag
        val itemId = v?.tag as Long

        // By action (Safe Args)
        val action = SearchFragmentDirections.actionSearchFragmentToDetailsFragment(itemId)
        this.findNavController().navigate(action)
    }

    // -- RecyclerView --

    /**
     * Configures the RecyclerView
     */
    private fun configureRecyclerView() {
        // Adapter
        this.mAdapter = RealEstateAdapter(
            mCallback = this@SearchFragment
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
        with(this.mRootView.fragment_search_RecyclerView) {
            setHasFixedSize(true)
            layoutManager = viewManager
            addItemDecoration(divider)
            adapter = this@SearchFragment.mAdapter
        }
    }

    // -- Listener of components --

    /**
     * Configures the listener of Each component
     */
    private fun configureListenerOfEachComponent() {
        // FAB
        this.mRootView.fragment_search_fab.setOnClickListener {
            this.actionToSearchRealEstates()
        }
    }

    // -- LiveData --

    /**
     * Configures the LiveData thanks to a simple format
     */
    private fun configureRealEstateLiveData() {
        // todo - 18/04/2020 - Next feature: Fetch all real estates of all users
        this.mViewModel
            .getRealEstatesWithPhotosByUserId(userId = 1L)
            .observe(
                this.viewLifecycleOwner,
                Observer {
                    this.mAllDataFromDatabase = it
                    this.configureUI(it)
                }
            )
    }

    // -- Search Real Estate --

    private fun actionToSearchRealEstates() {
        Timber.d("SEARCH")
    }

    // -- UI --

    /**
     * Configures UI
     * @param realEstatesWithPhotos a [List] of [RealEstateWithPhotos]
     */
    private fun configureUI(realEstatesWithPhotos: List<RealEstateWithPhotos>) {
        Timber.d("UI")
        this.mAdapter.updateData(realEstatesWithPhotos)
    }
}
