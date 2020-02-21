package com.mancel.yann.realestatemanager.utils

import androidx.recyclerview.widget.DiffUtil

/**
 * Created by Yann MANCEL on 21/02/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.utils
 */
class PhotoDiffCallback(private val mOldList: List<String>,
                        private val mNewList: List<String>) : DiffUtil.Callback() {

    // METHODS -------------------------------------------------------------------------------------

    override fun getOldListSize(): Int = this.mOldList.size

    override fun getNewListSize(): Int = this.mNewList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Comparison based on:

        return false
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Comparison based on:

        return false
    }
}