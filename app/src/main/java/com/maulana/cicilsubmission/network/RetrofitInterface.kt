package com.maulana.cicilsubmission.network

import com.maulana.cicilsubmission.model.MovieDetailModel
import com.maulana.cicilsubmission.model.ResponseMovieModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface RetrofitInterface {

    @GET("?")
    fun getListMovie(@QueryMap options: Map<String, String>): Call<ResponseMovieModel>

    @GET("?")
    fun getDetail(@QueryMap options: Map<String, String>): Call<MovieDetailModel>
}