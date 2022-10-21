package com.sachinverma.moengageassignment.model

import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("status")
    var status: String,
    @SerializedName("totalResults")
    var totalResults: Int,
    @SerializedName("articles")
    var articles: List<News>
)
