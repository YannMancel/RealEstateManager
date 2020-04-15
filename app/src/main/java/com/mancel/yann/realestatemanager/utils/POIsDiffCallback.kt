package com.mancel.yann.realestatemanager.utils

import androidx.recyclerview.widget.DiffUtil
import com.mancel.yann.realestatemanager.models.PointOfInterest

/**
 * Created by Yann MANCEL on 21/02/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.utils
 *
 * A [DiffUtil.Callback] subclass.
 */
class POIsDiffCallback(
    private val mOldList: List<PointOfInterest>,
    private val mNewList: List<PointOfInterest>
) : DiffUtil.Callback() {

    // METHODS -------------------------------------------------------------------------------------

    override fun getOldListSize(): Int = this.mOldList.size

    override fun getNewListSize(): Int = this.mNewList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Comparison based on Name:
        val oldName = this.mOldList[oldItemPosition].mName
        val newName = this.mNewList[newItemPosition].mName

        return oldName == newName
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Comparison on all fields
        return this.mOldList[oldItemPosition] == this.mNewList[newItemPosition]
    }
}