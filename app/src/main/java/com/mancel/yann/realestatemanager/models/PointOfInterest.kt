package com.mancel.yann.realestatemanager.models

import androidx.room.*

/**
 * Created by Yann MANCEL on 02/03/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.models
 *
 * It is the point of interest at proximity (school, park, business...).
 *
 * Many-to-many relationships: many [RealEstate] and many [PointOfInterest]
 */
@Entity(tableName = "point_of_interest",
        indices = [Index(value = ["name",
                                  "loc_latitude", "loc_longitude"],
                         unique = true)])
data class PointOfInterest(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_point_of_interest")
    var mId: Long = 0L,

    @ColumnInfo(name = "name")
    var mName: String? = null,

    @ColumnInfo(name = "url_picture")
    var mUrlPicture: String? = null,

    @Embedded(prefix = "loc_")
    var mAddress: Address? = null,

    @Ignore
    var mIsSelected: Boolean = true
) {
    // NESTED CLASSES ------------------------------------------------------------------------------

    /**
     * A [Comparator] of [PointOfInterest] subclass.
     */
    class AZTitleComparator : Comparator<PointOfInterest> {
        override fun compare(left: PointOfInterest?, right: PointOfInterest?): Int {
            // Comparison on the name
            val titleLeft = left?.mName ?: ""
            val titleRight = right?.mName ?: ""

            return titleLeft.compareTo(titleRight, ignoreCase = true)
        }
    }
}