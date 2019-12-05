package com.maulana.cicilsubmission.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MovieModel (
    @SerializedName("Title")
    var title: String,
    @SerializedName("Year")
    var year: String,
    @SerializedName("imdbID")
    var imdbId: String,
    @SerializedName("Type")
    var type: String,
    @SerializedName("Poster")
    var poster: String
): Serializable

open class ResponseMovieModel(
    @SerializedName("Search")
    var search: List<MovieModel>
): Serializable