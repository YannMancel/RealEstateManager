package com.mancel.yann.realestatemanager.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by Yann MANCEL on 15/04/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.models
 */

/*
    Pojo structure:
    NearbySearch
    |
    +--- html_attributions: List<Any>
    |
    +--- next_page_token: String
    |
    +--- results: List<Result>
    |    |
    |    +--- geometry
    |    |    |
    |    |    +--- location: Location
    |    |    |    |
    |    |    |    +--- lat: Double
    |    |    |    |
    |    |    |    +--- lng: Double
    |    |    |
    |    |    +--- viewport: ViewPort
    |    |         |
    |    |         +--- northeast: Northeast
    |    |         |    |
    |    |         |    +--- lat: Double
    |    |         |    |
    |    |         |    +--- lng: Double
    |    |         |
    |    |         +--- southwest: Southwest
    |    |              |
    |    |              +--- lat: Double
    |    |              |
    |    |              +--- lng: Double
    |    |
    |    +--- icon: String
    |    |
    |    +--- id: String
    |    |
    |    +--- name: String
    |    |
    |    +--- opening_hours: Opening_hours
    |    |    |
    |    |    +--- open_now: Boolean
    |    |
    |    +--- photos: List<Photo>
    |    |    |
    |    |    +--- height: Int
    |    |    |
    |    |    +--- html_attributions: List<String>
    |    |    |
    |    |    +--- photo_reference: String
    |    |    |
    |    |    +--- width: Int
    |    |
    |    +--- place_id: String
    |    |
    |    +--- plus_code: PlusCode
    |    |    |
    |    |    +--- compound_code: String
    |    |    |
    |    |    +--- global_code: String
    |    |
    |    +--- rating: Double
    |    |
    |    +--- reference: String
    |    |
    |    +--- scope: String
    |    |
    |    +--- types: List<String>
    |    |
    |    +--- user_ratings_total: Integer
    |    |
    |    +--- vicinity: String
    |    |
    |    +--- price_level: Int
    |
    +--- status: String
*/

@JsonClass(generateAdapter = true)
data class NearbySearch(
    @Json(name = "html_attributions")
    var htmlAttributions:List<Any>? = null,
    @Json(name = "next_page_token")
    var nextPageTokenval: String? = null,
    @Json(name = "results")
    var results: List<Result>? = null,
    @Json(name = "status")
    var status: String? = null
)

@JsonClass(generateAdapter = true)
data class Result(
    @Json(name = "geometry")
    var geometry: Geometry? = null,
    @Json(name = "icon")
    var icon: String? = null,
    @Json(name = "id")
    var id: String? = null,
    @Json(name = "name")
    var name: String? = null,
    @Json(name = "photos")
    var photos: List<PhotoGM>? = null,
    @Json(name = "place_id")
    var placeId: String? = null,
    @Json(name = "reference")
    var reference: String? = null,
    @Json(name = "scope")
    var scope: String? = null,
    @Json(name = "types")
    var types: List<String>? = null,
    @Json(name = "vicinity")
    var vicinity: String? = null,
    @Json(name = "plus_code")
    var plusCode: PlusCode? = null,
    @Json(name = "opening_hours")
    var openingHours: OpeningHours? = null,
    @Json(name = "rating")
    var rating: Double? = null,
    @Json(name = "user_ratings_total")
    var userRatingsTotal: Int? = null,
    @Json(name = "price_level")
    var priceLevel: Int? = null
)

@JsonClass(generateAdapter = true)
data class Geometry(
    @Json(name = "location")
    var location: Location? = null,
    @Json(name = "viewport")
    var viewport: Viewport? = null
)

@JsonClass(generateAdapter = true)
data class Location(
    @Json(name = "lat")
    var lat: Double? = null,
    @Json(name = "lng")
    var lng: Double? = null
)

@JsonClass(generateAdapter = true)
data class Viewport(
    @Json(name = "northeast")
    var northeast:  Northeast? = null,
    @Json(name = "southwest")
    var southwest: Southwest? = null
)

@JsonClass(generateAdapter = true)
data class Northeast(
    @Json(name = "lat")
    var lat: Double? = null,
    @Json(name = "lng")
    var lng: Double? = null
)

@JsonClass(generateAdapter = true)
data class Southwest(
    @Json(name = "lat")
    var lat: Double? = null,
    @Json(name = "lng")
    var lng: Double? = null
)

@JsonClass(generateAdapter = true)
data class PhotoGM(
    @Json(name = "height")
    var height: Int? = null,
    @Json(name = "html_attributions")
    var htmlAttributions: List<String>? = null,
    @Json(name = "photo_reference")
    var photoReference: String? = null,
    @Json(name = "width")
    var width: Int? = null
)

@JsonClass(generateAdapter = true)
data class OpeningHours(
    @Json(name = "open_now")
    var openNow: Boolean? = null
)

@JsonClass(generateAdapter = true)
data class PlusCode(
    @Json(name = "compound_code")
    var compoundCode: String? = null,
    @Json(name = "global_code")
    var globalCode: String? = null
)