package com.mancel.yann.realestatemanager.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mancel.yann.realestatemanager.R
import com.mancel.yann.realestatemanager.utils.RealEstateDiffCallback
import kotlinx.android.synthetic.main.item_real_estate.view.*
import java.lang.ref.WeakReference

/**
 * Created by Yann MANCEL on 21/02/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.views.adpaters
 *
 * A [RecyclerView.Adapter] subclass.
 */
class RealEstateAdapter(private val mCallback: AdapterListener?) : RecyclerView.Adapter<RealEstateAdapter.RealEstateViewHolder>() {

    // NESTED CLASSES ------------------------------------------------------------------------------

    /**
     * A [RecyclerView.ViewHolder] subclass.
     */
    class RealEstateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // FIELDS ----------------------------------------------------------------------------------

        lateinit var mCallback: WeakReference<AdapterListener?>
    }

    // FIELDS --------------------------------------------------------------------------------------

    private var mRealEstates: List<String> = mutableListOf()

    // METHODS -------------------------------------------------------------------------------------

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RealEstateViewHolder {
        // Creates the View thanks to the inflater
        val view = LayoutInflater.from(parent.context)
                                 .inflate(R.layout.item_real_estate, parent, false)

        return RealEstateViewHolder(view)
    }

    override fun onBindViewHolder(holder: RealEstateViewHolder, position: Int) {
        // Data
        val data = this.mRealEstates[position]

        // Callback
        holder.mCallback = WeakReference(this.mCallback)

        // CardView
        holder.itemView.item_real_estate_CardView.setOnClickListener {
            // Tag -> Data
            it.tag = data

            // Starts the callback
            holder.mCallback.get()?.onClick(it)
        }

        // Image
        Glide.with(holder.itemView)
             .load(R.drawable.ic_home)
             .centerCrop()
             .fallback(R.drawable.ic_add)
             .error(R.drawable.ic_edit)
             .into(holder.itemView.item_real_estate_image)

        // Type
        holder.itemView.item_real_estate_type.text = data

        // City
        holder.itemView.item_real_estate_city.text = data

        // Price
        holder.itemView.item_real_estate_price.text = data
    }

    override fun getItemCount(): Int = this.mRealEstates.size

    // -- Real Estate --

    /**
     * Updates data of [RealEstateAdapter]
     * @param newRealEstates a [List] of [String]
     */
    fun updateData(newRealEstates: List<String>) {
        // Optimizes the performances of RecyclerView
        val diffCallback  = RealEstateDiffCallback(this.mRealEstates, newRealEstates)
        val diffResult  = DiffUtil.calculateDiff(diffCallback )

        // New data
        this.mRealEstates = newRealEstates

        // Notifies adapter
        diffResult.dispatchUpdatesTo(this)

        // Callback
        this.mCallback?.onDataChanged()
    }
}