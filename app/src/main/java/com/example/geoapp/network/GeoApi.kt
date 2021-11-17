package com.example.geoapp.network

import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody
import retrofit2.http.GET

interface GeoApi {
    @GET("russia.geo.json")
    fun getRussia(): Single<ResponseBody>
}