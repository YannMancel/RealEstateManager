package com.mancel.yann.realestatemanager.utils

import androidx.recyclerview.widget.DiffUtil
import com.mancel.yann.realestatemanager.models.RealEstateWithPhotos

/**
 * Created by Yann MANCEL on 21/02/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.utils
 *
 * A [DiffUtil.Callback] subclass.
 */
class RealEstateDiffCallback(
    private val mOldList: List<RealEstateWithPhotos>,
    private val mNewList: List<RealEstateWithPhotos>
) : DiffUtil.Callback() {

    // METHODS -------------------------------------------------------------------------------------

    override fun getOldListSize(): Int = this.mOldList.size

    override fun getNewListSize(): Int = this.mNewList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Comparison based on Id:
        val oldId = this.mOldList[oldItemPosition].mRealEstate?.mId ?: 0L
        val newId = this.mNewList[newItemPosition].mRealEstate?.mId ?: 0L

        return oldId == newId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Comparison on all fields
        return this.mOldList[oldItemPosition] == this.mNewList[newItemPosition]
    }
}