package com.guru.bonial.domain

import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList

import com.guru.bonial.view.news.utils.PagedListWrapper
import com.guru.bonial.network.ApiService
import com.guru.bonial.view.news.model.NewsDataModel
import javax.inject.Inject

class NewsRepository
@Inject constructor(private val api: ApiService) : INewsRepository {

    override fun getNews(pageSize: Int): PagedListWrapper<NewsDataModel> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPrefetchDistance(5)
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize)
            .build()

        val factory = NewsDataSourceFactory(api)

        val pagedList = LivePagedListBuilder(factory, config)
            .build()

        return PagedListWrapper(
            pagedList,
            loadingState = Transformations.switchMap(factory.liveData) {
                it.loadingState
            },
            load = {
                factory.liveData.value?.invalidate()
            }
        )
    }
}
