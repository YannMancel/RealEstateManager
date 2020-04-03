package com.mancel.yann.realestatemanager.utils

import androidx.recyclerview.widget.DiffUtil
import com.mancel.yann.realestatemanager.models.Photo

/**
 * Created by Yann MANCEL on 21/02/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.utils
 *
 * A [DiffUtil.Callback] subclass.
 */
class PhotoDiffCallback(
    private val mOldList: List<Photo>,
    private val mNewList: List<Photo>
) : DiffUtil.Callback() {

    // METHODS -------------------------------------------------------------------------------------

    override fun getOldListSize(): Int = this.mOldList.size

    override fun getNewListSize(): Int = this.mNewList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Comparison based on Id:
        val oldId = this.mOldList[oldItemPosition].mId
        val newId = this.mNewList[newItemPosition].mId

        return oldId == newId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Comparison based on:
        //  - the Id
        //  - the url
        //  - the description
        //  - the real estate id

        return this.mOldList[oldItemPosition] == this.mNewList[newItemPosition]
    }
}