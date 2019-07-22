package com.guru.bonial.domain

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.guru.bonial.network.ApiService
import com.guru.bonial.view.news.model.NewsDataModel

class NewsDataSourceFactory
constructor(private val api: ApiService) : DataSource.Factory<Int, NewsDataModel>() {
    val liveData = MutableLiveData<NewsDataSource>()

    override fun create(): DataSource<Int, NewsDataModel> {
        val source = NewsDataSource(api)
        liveData.postValue(source)
        return source
    }
}
