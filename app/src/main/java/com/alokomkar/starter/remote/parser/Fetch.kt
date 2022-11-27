package com.alokomkar.starter.remote.parser


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Fetch(
    @SerializedName("total")
    val total: Int
)