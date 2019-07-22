package com.guru.bonial.view.news.adapter

import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.guru.bonial.R
import com.guru.bonial.view.news.utils.LoadingState
import com.guru.bonial.view.news.toVisibility

class ProgressViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar)

    fun bindData(state: LoadingState?) {
        progressBar.toVisibility(state == LoadingState.LOADING)
    }
}
