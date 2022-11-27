package com.alokomkar.starter.remote.parser


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class ProcessingTimingsMS(
    @SerializedName("afterFetch")
    val afterFetch: AfterFetch,
    @SerializedName("fetch")
    val fetch: Fetch,
    @SerializedName("total")
    val total: Int
)