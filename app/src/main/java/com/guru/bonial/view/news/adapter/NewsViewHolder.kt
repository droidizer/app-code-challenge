package com.guru.bonial.view.news.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.guru.bonial.R
import com.guru.bonial.view.news.model.NewsDataModel
import com.squareup.picasso.Picasso

class NewsViewHolder(private val view: View,
                     private val callback: (NewsDataModel) -> Unit) : RecyclerView.ViewHolder(view) {

    private val title = view.findViewById<TextView>(R.id.title)
    private val subtitle = view.findViewById<TextView>(R.id.subtitle)
    private val image = view.findViewById<ImageView>(R.id.newsCover)

    fun bindData(newsData: NewsDataModel?) {
        newsData ?: return
        title.text = newsData.title
        subtitle.text = newsData.description

        Picasso.get()
            .load(newsData.urlToImage)
            .into(image)

        view.setOnClickListener {
            callback(newsData)
        }
    }
}
