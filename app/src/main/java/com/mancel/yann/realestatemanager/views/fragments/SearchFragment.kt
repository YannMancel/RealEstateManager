package com.mancel.yann.realestatemanager.views.fragments

import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.checkbox.MaterialCheckBox
import com.mancel.yann.realestatemanager.R
import com.mancel.yann.realestatemanager.models.RealEstateWithPhotos
import com.mancel.yann.realestatemanager.views.adapters.AdapterListener
import com.mancel.yann.realestatemanager.views.adapters.RealEstateAdapter
import kotlinx.android.synthetic.main.fragment_search.view.*

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

    companion object {
        private const val MIN_PRICE = 1
        private const val MAX_PRICE = 100_000_000
        private const val MIN_SURFACE = 1
        private const val MAX_SURFACE = 10_000
        private const val MIN_ROOM = 1
        private const val MAX_ROOM = 100
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- BaseFragment --

    @LayoutRes
    override fun getFragmentLayout(): Int = R.layout.fragment_search

    override fun configureDesign(savedInstanceState: Bundle?) {
        // UI
        this.configureRecyclerView()
        this.configureListenerOfEachComponent()

        // LiveData
        this.configureMultiSearchLiveData(savedInstanceState)
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
        // Checkbox: Price min
        this.configureCheckBox(
            this.mRootView.fragment_search_checkbox_min_price,
            this.mRootView.fragment_search_min_price,
            this.mRootView.fragment_search_seekBar_price_min
        )

        // Checkbox: Price max
        this.configureCheckBox(
            this.mRootView.fragment_search_checkbox_max_price,
            this.mRootView.fragment_search_max_price,
            this.mRootView.fragment_search_seekBar_price_max
        )

        // Checkbox: Surface min
        this.configureCheckBox(
            this.mRootView.fragment_search_checkbox_min_surface,
            this.mRootView.fragment_search_min_surface,
            this.mRootView.fragment_search_seekBar_min_surface
        )

        // Checkbox: Surface max
        this.configureCheckBox(
            this.mRootView.fragment_search_checkbox_max_surface,
            this.mRootView.fragment_search_max_surface,
            this.mRootView.fragment_search_seekBar_max_surface
        )

        // Checkbox: Room min
        this.configureCheckBox(
            this.mRootView.fragment_search_checkbox_min_room,
            this.mRootView.fragment_search_min_room,
            this.mRootView.fragment_search_seekBar_min_room
        )

        // Checkbox: Room max
        this.configureCheckBox(
            this.mRootView.fragment_search_checkbox_max_room,
            this.mRootView.fragment_search_max_room,
            this.mRootView.fragment_search_seekBar_max_room
        )

        // SeekBar: Price min
        this.configureSeekBar(
            seekBar = this.mRootView.fragment_search_seekBar_price_min,
            maxValue = MAX_PRICE,
            progressValue = MIN_PRICE,
            textView = this.mRootView.fragment_search_min_price,
            unit = "\$"
        )

        // SeekBar: Price max
        this.configureSeekBar(
            seekBar = this.mRootView.fragment_search_seekBar_price_max,
            maxValue = MAX_PRICE,
            progressValue = MAX_PRICE,
            textView = this.mRootView.fragment_search_max_price,
            unit = "\$"
        )

        // SeekBar: Surface min
        this.configureSeekBar(
            seekBar = this.mRootView.fragment_search_seekBar_min_surface,
            maxValue = MAX_SURFACE,
            progressValue = MIN_SURFACE,
            textView = this.mRootView.fragment_search_min_surface,
            unit = "m2"
        )

        // SeekBar: Surface max
        this.configureSeekBar(
            seekBar = this.mRootView.fragment_search_seekBar_max_surface,
            maxValue = MAX_SURFACE,
            progressValue = MAX_SURFACE,
            textView = this.mRootView.fragment_search_max_surface,
            unit = "m2"
        )

        // SeekBar: Room min
        this.configureSeekBar(
            seekBar = this.mRootView.fragment_search_seekBar_min_room,
            maxValue = MAX_ROOM,
            progressValue = MIN_ROOM,
            textView = this.mRootView.fragment_search_min_room,
            unit = null
        )

        // SeekBar: Room max
        this.configureSeekBar(
            seekBar = this.mRootView.fragment_search_seekBar_max_room,
            maxValue = MAX_ROOM,
            progressValue = MAX_ROOM,
            textView = this.mRootView.fragment_search_max_room,
            unit = null
        )

        // FAB
        this.mRootView.fragment_search_fab.setOnClickListener {
            this.actionToSearchRealEstates()
        }
    }

    // -- LiveData --

    /**
     * Configures the liveData according to the [Bundle]
     * @param savedInstanceState a [Bundle] to check the configuration changes of Fragment
     */
    private fun configureMultiSearchLiveData(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            this.mViewModel
                .getMultiSearchEvenIfNull()?.observe(
                    this.viewLifecycleOwner,
                    Observer { this.configureUI(it) }
                )
        }
    }

    // -- CheckBox --

    /**
     * Configures a [MaterialCheckBox]
     * @param checkBox  a [MaterialCheckBox]
     * @param textView  a [TextView]
     * @param seekBar   a [SeekBar]
     */
    private fun configureCheckBox(
        checkBox: MaterialCheckBox,
        textView: TextView,
        seekBar: SeekBar
    ) {
        checkBox.setOnClickListener {
            if (it is MaterialCheckBox) {
                textView.isEnabled = it.isChecked
                seekBar.isEnabled = it.isChecked
            }
        }
    }

    // -- SeekBar --

    /**
     * Configure a [SeekBar]
     * @param seekBar       a [SeekBar]
     * @param maxValue      a [Int] that contains the max value
     * @param progressValue a [Int] that contains the current value
     * @param textView      a [TextView]
     * @param unit          a [String] that contains the unit
     */
    private fun configureSeekBar(
        seekBar: SeekBar,
        maxValue: Int,
        progressValue: Int,
        textView: TextView,
        unit: String?
    ) {
        with(seekBar) {
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    textView.text =
                        if (unit.isNullOrEmpty())
                            "$progress"
                        else
                            "$progress $unit"
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) { /* Do nothing */ }

                override fun onStopTrackingTouch(seekBar: SeekBar?) { /* Do nothing */ }
            })
            max = maxValue
            progress = progressValue
        }
    }

    // -- Search Real Estate --

    /**
     * Action to search the real estates after click of FAB
     */
    private fun actionToSearchRealEstates() {
        // Error: Price
        if (this.mRootView.fragment_search_seekBar_price_max.progress < this.mRootView.fragment_search_seekBar_price_min.progress
            && this.mRootView.fragment_search_checkbox_max_price.isChecked
        ) {
            this.mCallback?.showMessage(this.getString(R.string.error_into_price_seekbar))
            return
        }

        // Error: Surface
        if (this.mRootView.fragment_search_seekBar_max_surface.progress < this.mRootView.fragment_search_seekBar_min_surface.progress
            && this.mRootView.fragment_search_checkbox_max_surface.isChecked
        ) {
            this.mCallback?.showMessage(this.getString(R.string.error_into_surface_seekbar))
            return
        }

        // Error: Room
        if (this.mRootView.fragment_search_seekBar_max_room.progress < this.mRootView.fragment_search_seekBar_min_room.progress
            && this.mRootView.fragment_search_checkbox_max_room.isChecked
        ) {
            this.mCallback?.showMessage(this.getString(R.string.error_into_room_seekbar))
            return
        }

        // Remove observer
        this.mViewModel.removeObserversOfMultiSearch(this.viewLifecycleOwner)

        // Reset LiveData
        this.mViewModel.resetMultiSearch()

        this.mViewModel
            .getRealEstatesWithPhotosByMultiSearch(
                minPrice =
                    if (this.mRootView.fragment_search_checkbox_min_price.isChecked)
                        this.mRootView.fragment_search_seekBar_price_min.progress.toDouble()
                    else
                        MIN_PRICE.toDouble(),
                maxPrice =
                    if (this.mRootView.fragment_search_checkbox_max_price.isChecked)
                        this.mRootView.fragment_search_seekBar_price_max.progress.toDouble()
                    else
                        MAX_PRICE.toDouble(),
                minSurface =
                    if (this.mRootView.fragment_search_checkbox_min_surface.isChecked)
                        this.mRootView.fragment_search_seekBar_min_surface.progress.toDouble()
                    else
                        MIN_SURFACE.toDouble(),
                maxSurface =
                    if (this.mRootView.fragment_search_checkbox_max_surface.isChecked)
                        this.mRootView.fragment_search_seekBar_max_surface.progress.toDouble()
                    else
                        MAX_SURFACE.toDouble(),
                minNumberRoom =
                    if (this.mRootView.fragment_search_checkbox_min_room.isChecked)
                        this.mRootView.fragment_search_seekBar_min_room.progress
                    else
                        MIN_ROOM,
                maxNumberRoom =
                    if (this.mRootView.fragment_search_checkbox_max_room.isChecked)
                        this.mRootView.fragment_search_seekBar_max_room.progress
                    else
                        MAX_ROOM
            )
            .observe(
                this.viewLifecycleOwner,
                Observer { this.configureUI(it) }
            )
    }

    // -- UI --

    /**
     * Configures UI
     * @param realEstatesWithPhotos a [List] of [RealEstateWithPhotos]
     */
    private fun configureUI(realEstatesWithPhotos: List<RealEstateWithPhotos>) {
        this.mAdapter.updateData(realEstatesWithPhotos)
    }
}