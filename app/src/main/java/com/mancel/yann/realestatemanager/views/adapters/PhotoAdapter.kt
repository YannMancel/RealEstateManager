package com.mancel.yann.realestatemanager.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mancel.yann.realestatemanager.R
import com.mancel.yann.realestatemanager.utils.PhotoDiffCallback
import kotlinx.android.synthetic.main.item_photo.view.*
import java.lang.ref.WeakReference

/**
 * Created by Yann MANCEL on 21/02/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.views.adpaters
 *
 * A [RecyclerView.Adapter] subclass.
 */
class PhotoAdapter(private val mCallback: AdapterListener? = null) : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    // NESTED CLASSES ------------------------------------------------------------------------------

    /**
     * A [RecyclerView.ViewHolder] subclass.
     */
    class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // FIELDS ----------------------------------------------------------------------------------

        lateinit var mCallback: WeakReference<AdapterListener?>
    }

    // FIELDS --------------------------------------------------------------------------------------

    private var mPhotos: List<String> = mutableListOf()

    // METHODS -------------------------------------------------------------------------------------

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        // Creates the View thanks to the inflater
        val view = LayoutInflater.from(parent.context)
                                 .inflate(R.layout.item_photo, parent, false)

        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        // Data
        val data = this.mPhotos[position]

        // Callback
        holder.mCallback = WeakReference(this.mCallback)

        // CardView
        holder.itemView.item_photo_CardView.setOnClickListener {
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
             .into(holder.itemView.item_photo_image)

        // Description
        holder.itemView.item_photo_description.text = data
    }

    override fun getItemCount(): Int = this.mPhotos.size

    // -- Photo --

    /**
     * Updates data of [PhotoAdapter]
     * @param newPhotos a [List] of [String]
     */
    fun updateData(newPhotos: List<String>) {
        // Optimizes the performances of RecyclerView
        val diffCallback  = PhotoDiffCallback(this.mPhotos, newPhotos)
        val diffResult  = DiffUtil.calculateDiff(diffCallback )

        // New data
        this.mPhotos = newPhotos

        // Notifies adapter
        diffResult.dispatchUpdatesTo(this)

        // Callback
        this.mCallback?.onDataChanged()
    }
}