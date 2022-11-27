package com.alokomkar.starter.remote.parser


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class NewsParser(
    @SerializedName("exhaustive")
    val exhaustive: Exhaustive,
    @SerializedName("exhaustiveNbHits")
    val exhaustiveNbHits: Boolean,
    @SerializedName("exhaustiveTypo")
    val exhaustiveTypo: Boolean,
    @SerializedName("hits")
    val hits: List<Hit>,
    @SerializedName("hitsPerPage")
    val hitsPerPage: Int,
    @SerializedName("nbHits")
    val nbHits: Int,
    @SerializedName("nbPages")
    val nbPages: Int,
    @SerializedName("page")
    val page: Int,
    @SerializedName("params")
    val params: String,
    @SerializedName("processingTimeMS")
    val processingTimeMS: Int,
    @SerializedName("processingTimingsMS")
    val processingTimingsMS: ProcessingTimingsMS,
    @SerializedName("query")
    val query: String
)