package com.guru.bonial.view.news.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NewsDataModel(
    var author: String? = "",
    var content: String? = "",
    var description: String? = "",
    var publishedAt: String? = "",
    var title: String? = "",
    var url: String? = "",
    var urlToImage: String? = ""
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
