package com.alokomkar.starter.remote.parser


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Exhaustive(
    @SerializedName("nbHits")
    val nbHits: Boolean,
    @SerializedName("typo")
    val typo: Boolean
)