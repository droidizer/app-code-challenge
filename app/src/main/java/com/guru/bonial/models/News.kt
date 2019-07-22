package com.guru.bonial.models

import com.google.gson.annotations.SerializedName
import com.guru.bonial.view.news.model.NewsDataModel

data class News(
    val source: Source = Source(),
    @SerializedName("author")
    val author: String? = "",
    @SerializedName("content")
    val content: String? = "",
    @SerializedName("description")
    val description: String? = "",
    @SerializedName("publishedAt")
    val publishedAt: String? = "",
    @SerializedName("title")
    val title: String? = "",
    @SerializedName("url")
    val url: String? = "",
    @SerializedName("urlToImage")
    val urlToImage: String? = ""
)

fun News.toModel() = NewsDataModel(author, content, description, publishedAt, title, url, urlToImage)
