package com.mancel.yann.realestatemanager.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mancel.yann.realestatemanager.R
import com.mancel.yann.realestatemanager.models.IdTypeAddressPriceTupleOfRealEstate
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

    private var mRealEstates = emptyList<IdTypeAddressPriceTupleOfRealEstate>()

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
     * @param realEstate    a [IdTypeAddressPriceTupleOfRealEstate]
     */
    private fun configureDesign(holder: RealEstateViewHolder, realEstate: IdTypeAddressPriceTupleOfRealEstate) {
        // CardView
        holder.itemView.item_real_estate_CardView.setOnClickListener {
            // Tag -> Data's Id
            it.tag = realEstate.mId

            // Starts the callback
            holder.mCallback.get()?.onClick(it)
        }

        // Image
        // todo Add photo
        Glide.with(holder.itemView)
            .load(R.drawable.ic_home)
            .centerCrop()
            .fallback(R.drawable.ic_add)
            .error(R.drawable.ic_edit)
            .into(holder.itemView.item_real_estate_image)

        // Type
        realEstate.mType?.let { holder.itemView.item_real_estate_type.text = it }

        // City
        realEstate.mAddress?.mCity?.let { holder.itemView.item_real_estate_city.text = it }

        // Price
        realEstate.mPrice?.let { holder.itemView.item_real_estate_price.text = it.toString() }
    }

    // -- Real Estate --

    /**
     * Updates data of [RealEstateAdapter]
     * @param newRealEstates a [List] of [IdTypeAddressPriceTupleOfRealEstate]
     */
    fun updateData(newRealEstates: List<IdTypeAddressPriceTupleOfRealEstate>) {
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