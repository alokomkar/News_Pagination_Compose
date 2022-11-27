package com.alokomkar.starter.remote.parser


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class HighlightResult(
    @SerializedName("author")
    val author: Author,
    @SerializedName("story_text")
    val storyText: StoryText,
    @SerializedName("title")
    val title: Title,
    @SerializedName("url")
    val url: Url
)