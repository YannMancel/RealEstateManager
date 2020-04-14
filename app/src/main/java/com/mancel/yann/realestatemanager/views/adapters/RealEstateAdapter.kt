package com.mancel.yann.realestatemanager.views.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mancel.yann.realestatemanager.R
import com.mancel.yann.realestatemanager.models.RealEstateWithPhotos
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
class RealEstateAdapter(
    private val mCallback: AdapterListener? = null
) : RecyclerView.Adapter<RealEstateAdapter.RealEstateViewHolder>() {

    // FIELDS --------------------------------------------------------------------------------------

    private var mRealEstates = emptyList<RealEstateWithPhotos>()

    // METHODS -------------------------------------------------------------------------------------

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RealEstateViewHolder {
        // Creates the View thanks to the inflater
        val view = LayoutInflater.from(parent.context)
                                 .inflate(R.layout.item_real_estate, parent, false)

        return RealEstateViewHolder(view, WeakReference(this.mCallback))
    }

    override fun onBindViewHolder(holder: RealEstateViewHolder, position: Int) {
        val realEstate = this.mRealEstates[position]
        this.configureDesign(holder, realEstate)
    }

    override fun getItemCount(): Int = this.mRealEstates.size

    // -- Design item --

    /**
     * Configures the design of each item
     * @param holder        a [RealEstateViewHolder] that corresponds to the item
     * @param realEstate    a [RealEstateWithPhotos]
     */
    private fun configureDesign(holder: RealEstateViewHolder, realEstate: RealEstateWithPhotos) {
        // CardView
        holder.itemView.item_real_estate_CardView.setOnClickListener {
            // Background of item
            this.configureBackgroundItem(realEstate)

            // Tag -> Data's Id
            it.tag = realEstate.mRealEstate?.mId

            // Starts the callback
            holder.mCallback.get()?.onClick(it)
        }

        // Image
        realEstate.mPhotos?.let {
            if (it.isNotEmpty()) {
                Glide.with(holder.itemView)
                     .load(it[0].mUrlPicture)
                     .centerCrop()
                     .placeholder(R.drawable.placeholder_image)
                     .fallback(R.drawable.ic_photo)
                     .error(R.drawable.ic_clear)
                     .into(holder.itemView.item_real_estate_image)
            }
            else {
                holder.itemView.item_real_estate_image.setImageResource(R.drawable.ic_photo)
            }
        }

        // Type
        realEstate.mRealEstate?.mType?.let { holder.itemView.item_real_estate_type.text = it }

        // City
        realEstate.mRealEstate?.mAddress?.mCity?.let { holder.itemView.item_real_estate_city.text = it }

        // Price
        realEstate.mRealEstate?.mPrice?.let { holder.itemView.item_real_estate_price.text = it.toString() }

        // Background color
        realEstate.mRealEstate?.mIsSelected?.let { isSelected ->
            // CardView
            holder.itemView.item_real_estate_CardView.setCardBackgroundColor(
                if (isSelected)
                    ContextCompat.getColor(holder.itemView.context, R.color.colorAccent)
                else
                    Color.WHITE
            )

            // TextView
            holder.itemView.item_real_estate_price.setTextColor(
                if (isSelected)
                    Color.WHITE
                else
                    ContextCompat.getColor(holder.itemView.context, R.color.colorAccent)
            )
        }
    }

    /**
     * Configures the background of item
     * @param realEstate a [RealEstateWithPhotos]
     */
    private fun configureBackgroundItem(realEstate: RealEstateWithPhotos) {
        realEstate.mRealEstate?.mId?.let { id ->
            // Create a new List
            val newRealEstates = mutableListOf<RealEstateWithPhotos>().apply {
                // Add all items
                this@RealEstateAdapter.mRealEstates.forEach {
                    val item = it.mRealEstate?.copy()
                    this.add(it.copy(mRealEstate = item))
                }

                // Reset on the previous item selected
                forEach {
                    it.mRealEstate?.mIsSelected = it.mRealEstate?.mId == id
                }
            }

            // Update UI
            this.updateData(newRealEstates)
        }
    }

    // -- Real Estate --

    /**
     * Updates data of [RealEstateAdapter]
     * @param newRealEstates a [List] of [RealEstateWithPhotos]
     */
    fun updateData(newRealEstates: List<RealEstateWithPhotos>) {
        // Optimizes the performances of RecyclerView
        val diffCallback  = RealEstateDiffCallback(this.mRealEstates, newRealEstates)
        val diffResult  = DiffUtil.calculateDiff(diffCallback )

        // New data
        this.mRealEstates = newRealEstates

        // Notifies adapter
        diffResult.dispatchUpdatesTo(this@RealEstateAdapter)

        // Callback
        this.mCallback?.onDataChanged()
    }

    // NESTED CLASSES ------------------------------------------------------------------------------

    /**
     * A [RecyclerView.ViewHolder] subclass.
     */
    class RealEstateViewHolder(
        itemView: View,
        var mCallback: WeakReference<AdapterListener?>
    ) : RecyclerView.ViewHolder(itemView)
}