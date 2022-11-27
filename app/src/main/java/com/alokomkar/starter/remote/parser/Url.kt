package com.alokomkar.starter.remote.parser


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Url(
    @SerializedName("fullyHighlighted")
    val fullyHighlighted: Boolean,
    @SerializedName("matchLevel")
    val matchLevel: String,
    @SerializedName("matchedWords")
    val matchedWords: List<String>,
    @SerializedName("value")
    val value: String
)