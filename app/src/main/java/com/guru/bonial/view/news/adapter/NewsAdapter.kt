package com.guru.bonial.view.news.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.guru.bonial.R
import com.guru.bonial.view.news.utils.LoadingState
import com.guru.bonial.view.news.model.NewsDataModel
import java.lang.IllegalStateException

class NewsAdapter constructor(
    private val itemClickCallback: (NewsDataModel) -> Unit
) :
    PagedListAdapter<NewsDataModel, RecyclerView.ViewHolder>(POST_COMPARATOR) {

    private var loadingState: LoadingState? = null

    fun hasExtraRow() = loadingState != null
            && loadingState != LoadingState.SUCCESS

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.news_item -> NewsViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(viewType, parent, false),
                itemClickCallback
            )
            R.layout.progress_item -> ProgressViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(viewType, parent, false)
            )
            else -> throw IllegalStateException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NewsViewHolder -> holder.bindData(getItem(position))
            is ProgressViewHolder -> holder.bindData(loadingState)
        }
    }

    override fun getItemCount(): Int {
        return when (hasExtraRow()) {
            true -> 1
            false -> 0
        } + super.getItemCount()
    }

    fun latestLoadingState(loadingState: LoadingState?) {
        val previousState = this.loadingState
        val hadExtraRow = hasExtraRow()
        this.loadingState = loadingState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != loadingState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.progress_item
        } else {
            R.layout.news_item
        }
    }

    companion object {
        val POST_COMPARATOR = object : DiffUtil.ItemCallback<NewsDataModel>() {
            override fun areContentsTheSame(oldItem: NewsDataModel, newItem: NewsDataModel): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: NewsDataModel, newItem: NewsDataModel): Boolean =
                oldItem == newItem
        }
    }
}
