package com.mancel.yann.realestatemanager.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mancel.yann.realestatemanager.R
import com.mancel.yann.realestatemanager.models.PointOfInterest
import com.mancel.yann.realestatemanager.utils.POIsDiffCallback
import kotlinx.android.synthetic.main.item_poi.view.*
import java.lang.ref.WeakReference

/**
 * Created by Yann MANCEL on 21/02/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.views.adpaters
 *
 * A [RecyclerView.Adapter] subclass.
 */
class POIsAdapter(
    private val mCallback: AdapterListener? = null
) : RecyclerView.Adapter<POIsAdapter.POIsViewHolder>() {

    // FIELDS --------------------------------------------------------------------------------------

    private val mPOIs = mutableListOf<PointOfInterest>()

    // METHODS -------------------------------------------------------------------------------------

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): POIsViewHolder {
        // Creates the View thanks to the inflater
        val view = LayoutInflater.from(parent.context)
                                 .inflate(R.layout.item_poi, parent, false)

        return POIsViewHolder(view, WeakReference(this.mCallback))
    }

    override fun onBindViewHolder(holder: POIsViewHolder, position: Int) {
        this.configureDesign(holder, this.mPOIs[position])
    }

    override fun getItemCount(): Int = this.mPOIs.size

    // -- Design item --

    /**
     * Configures the design of each item
     * @param holder    a [POIsViewHolder] that corresponds to the item
     * @param poi       a [PointOfInterest]
     */
    private fun configureDesign(holder: POIsViewHolder, poi: PointOfInterest) {
        // Name
        poi.mName?.let { holder.itemView.item_poi_name.text = it }

        // Is selected
        holder.itemView.item_poi_is_selected.isChecked = poi.mIsSelected
    }

    // -- Point of interest --

    /**
     * Updates data of [POIsAdapter]
     * @param newPOIs a [List] of [PointOfInterest]
     */
    fun updateData(newPOIs: List<PointOfInterest>) {
        // Optimizes the performances of RecyclerView
        val diffCallback  = POIsDiffCallback(this.mPOIs, newPOIs)
        val diffResult  = DiffUtil.calculateDiff(diffCallback )

        // New data
        this.mPOIs.clear()
        this.mPOIs.addAll(newPOIs)

        // Notifies adapter
        diffResult.dispatchUpdatesTo(this@POIsAdapter)

        // Callback
        this.mCallback?.onDataChanged()
    }

    // NESTED CLASSES ------------------------------------------------------------------------------

    /**
     * A [RecyclerView.ViewHolder] subclass.
     */
    class POIsViewHolder(
        itemView: View,
        var mCallback: WeakReference<AdapterListener?>
    ) : RecyclerView.ViewHolder(itemView)
}