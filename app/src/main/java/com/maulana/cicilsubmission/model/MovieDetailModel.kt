package com.maulana.cicilsubmission.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class MovieDetailModel(
    @SerializedName("Title")
    var title: String,
    @SerializedName("Genre")
    var genre: String,
    @SerializedName("Runtime")
    var runtime: String,
    @SerializedName("Year")
    var year: String,
    @SerializedName("Poster")
    var poster: String,
    @SerializedName("Plot")
    var plot: String
): Serializable