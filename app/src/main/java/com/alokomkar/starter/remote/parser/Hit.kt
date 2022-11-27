package com.alokomkar.starter.remote.parser


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Hit(
    @SerializedName("author")
    val author: String,
    @SerializedName("comment_text")
    val commentText: Any,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("created_at_i")
    val createdAtI: Int,
    @SerializedName("_highlightResult")
    val highlightResult: HighlightResult,
    @SerializedName("num_comments")
    val numComments: Int,
    @SerializedName("objectID")
    val objectID: String,
    @SerializedName("parent_id")
    val parentId: Any,
    @SerializedName("points")
    val points: Int,
    @SerializedName("relevancy_score")
    val relevancyScore: Int,
    @SerializedName("story_id")
    val storyId: Any,
    @SerializedName("story_text")
    val storyText: String,
    @SerializedName("story_title")
    val storyTitle: Any,
    @SerializedName("story_url")
    val storyUrl: Any,
    @SerializedName("_tags")
    val tags: List<String>,
    @SerializedName("title")
    val title: String,
    @SerializedName("url")
    val url: String? = null
)