package com.mancel.yann.realestatemanager.api

import com.mancel.yann.realestatemanager.models.NearbySearch
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Yann MANCEL on 15/04/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.api
 */
interface GoogleMapsService {

    // FIELDS --------------------------------------------------------------------------------------

    companion object {

        private const val BASE_URL = "https://maps.googleapis.com/maps/api/"

        val retrofit: Retrofit = Retrofit.Builder()
                                         .baseUrl(BASE_URL)
                                         .addConverterFactory(MoshiConverterFactory.create())
                                         .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                                         .build()

        /**
         * Gets a photo request URL
         * @param photoReference    a [String] that contains a string identifier that uniquely identifies a photo
         * @param maxWidth          an [Int] that specifies the maximum desired width, in pixels, of the image
         * @param key               a [String] that contains your application's API key
         * @return a [String] that contains the photo request URL
         */
        fun getPhoto(
            photoReference: String,
            maxWidth: Int,
            key: String
        ) = "${BASE_URL}place/photo?photoreference=${photoReference}&maxwidth=${maxWidth}&key=${key}"
    }

    // METHODS -------------------------------------------------------------------------------------

    /**
     * Gets a Nearby Search request is an HTTP URL in Json format
     * @param location a [String] that contains the latitude/longitude around which to retrieve place information
     * @param radius   a [Double] that defines the distance (in meters) within which to return place results
     * @param types    a [String] that restricts the results to places matching the specified type
     * @param key      a [String] that contains your application's API key
     * @return an [Observable] of [NearbySearch]
     */
    @GET("place/nearbysearch/json?")
    fun getNearbySearch(
        @Query("location") location: String,
        @Query("radius") radius: Double,
        @Query("types") types: String,
        @Query("key") key: String
    ): Observable<NearbySearch>
}