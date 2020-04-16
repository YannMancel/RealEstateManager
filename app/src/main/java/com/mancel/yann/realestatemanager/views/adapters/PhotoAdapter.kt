package com.mancel.yann.realestatemanager.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mancel.yann.realestatemanager.R
import com.mancel.yann.realestatemanager.models.Photo
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
class PhotoAdapter(
    private val mCallback: AdapterListener? = null,
    private val mButtonDisplayMode: ButtonDisplayMode = ButtonDisplayMode.NORMAL_MODE
) : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    // ENUMS ---------------------------------------------------------------------------------------

    enum class ButtonDisplayMode {NORMAL_MODE, EDIT_MODE}

    // FIELDS --------------------------------------------------------------------------------------

    private val mPhotos = mutableListOf<Photo>()

    // METHODS -------------------------------------------------------------------------------------

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        // Creates the View thanks to the inflater
        val view = LayoutInflater.from(parent.context)
                                 .inflate(R.layout.item_photo, parent, false)

        return PhotoViewHolder(view, WeakReference(this.mCallback))
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = this.mPhotos[position]
        this.configureDesign(holder, photo)
    }

    override fun getItemCount(): Int = this.mPhotos.size

    // -- Design item --

    /**
     * Configures the design of each item
     * @param holder    a [PhotoViewHolder] that corresponds to the item
     * @param photo     a [Photo]
     */
    private fun configureDesign(holder: PhotoViewHolder, photo: Photo) {
        // Image
        photo.mUrlPicture?.let {
            Glide.with(holder.itemView)
                 .load(it)
                 .centerCrop()
                 .placeholder(R.drawable.placeholder_image)
                 .fallback(R.drawable.ic_photo)
                 .error(R.drawable.ic_clear)
                 .into(holder.itemView.item_photo_image)
        }

        // Description
        photo.mDescription?.let { holder.itemView.item_photo_description.text = it }

        // Buttons: Visibility
        val visibility = when (this.mButtonDisplayMode) {
            ButtonDisplayMode.NORMAL_MODE -> View.GONE
            ButtonDisplayMode.EDIT_MODE -> View.VISIBLE
        }

        holder.itemView.item_photo_delete_media.visibility = visibility
        holder.itemView.item_photo_edit_media.visibility = visibility

        // Here the listeners are not useful
        if (visibility == View.GONE) {
            return
        }

        // Button DELETE: add Listener
        holder.itemView.item_photo_delete_media.setOnClickListener {
            // Tag -> photo
            it.tag = photo

            // Starts the callback
            holder.mCallback.get()?.onClick(it)
        }

        // Button EDIT: add Listener
        holder.itemView.item_photo_edit_media.setOnClickListener {
            // Tag -> photo
            it.tag = photo

            // Starts the callback
            holder.mCallback.get()?.onClick(it)
        }
    }

    // -- Photo --

    /**
     * Updates data of [PhotoAdapter]
     * @param newPhotos a [List] of [Photo]
     */
    fun updateData(newPhotos: List<Photo>) {
        // Optimizes the performances of RecyclerView
        val diffCallback  = PhotoDiffCallback(this.mPhotos, newPhotos)
        val diffResult  = DiffUtil.calculateDiff(diffCallback )

        // New data
        this.mPhotos.clear()
        this.mPhotos.addAll(newPhotos)

        // Notifies adapter
        diffResult.dispatchUpdatesTo(this@PhotoAdapter)

        // Callback
        this.mCallback?.onDataChanged()
    }

    // NESTED CLASSES ------------------------------------------------------------------------------

    /**
     * A [RecyclerView.ViewHolder] subclass.
     */
    class PhotoViewHolder(
        itemView: View,
        var mCallback: WeakReference<AdapterListener?>
    ) : RecyclerView.ViewHolder(itemView)
}