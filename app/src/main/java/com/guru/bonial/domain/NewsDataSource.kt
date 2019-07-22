package com.guru.bonial.domain

import androidx.lifecycle.MutableLiveData
import androidx.paging.PositionalDataSource
import com.guru.bonial.network.ApiService
import com.guru.bonial.models.toModel
import com.guru.bonial.view.news.model.NewsDataModel
import com.guru.bonial.view.news.utils.LoadingState
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NewsDataSource(val api: ApiService) : PositionalDataSource<NewsDataModel>() {
    //***  Sources - https://medium.com/androidxx/propagate-data-and-state-using-mediatorlivedata-7ea25582fa29 ***//

    val loadingState = MutableLiveData<LoadingState>()
    private var initialLoadFailed = false

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<NewsDataModel>) {
        if (initialLoadFailed) {
            return
        }

        val nextPage = Math.ceil(params.startPosition / params.loadSize.toDouble()).toInt() + 1
        loadingState.postValue(LoadingState.LOADING)

        GlobalScope.launch {
            val result = getNews(nextPage, params.loadSize)
            loadingState.postValue(LoadingState.SUCCESS)
            if (result.isSuccess) {
                callback.onResult(result.getOrThrow())
            } else {
                loadingState.postValue(LoadingState.ERROR)
            }
        }
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<NewsDataModel>) {
        loadingState.postValue(LoadingState.LOADING)

        GlobalScope.launch {
            val result = getNews(1, params.pageSize)

            loadingState.postValue(LoadingState.SUCCESS)

            if (result.isSuccess) {
                callback.onResult(
                    result.getOrThrow(),
                    0, result.getOrThrow().size
                )
            } else {
                initialLoadFailed = true
                loadingState.postValue(LoadingState.ERROR)
            }
        }
    }

    private suspend fun getNews(page: Int, pageSize: Int): Result<List<NewsDataModel>> {
        return try {
            val paginatedResponse = api.getTopHeadlines(page, pageSize)
                .await()

            val modelList = mutableListOf<NewsDataModel>()
            val newsModel = paginatedResponse.articles.fold(modelList) { acc, news ->
                acc.add(news.toModel())
                acc
            }
            Result.success(newsModel)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
