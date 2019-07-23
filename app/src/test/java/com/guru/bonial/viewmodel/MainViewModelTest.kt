package com.guru.bonial.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.guru.bonial.api.MockApiService
import com.guru.bonial.domain.INewsRepository
import com.guru.bonial.domain.NewsRepository
import com.guru.bonial.models.News
import com.guru.bonial.models.PaginatedResponse
import com.guru.bonial.models.toModel
import com.guru.bonial.network.ApiService
import com.guru.bonial.view.news.model.NewsDataModel
import com.guru.bonial.view.news.utils.LoadingState
import com.guru.bonial.view.news.utils.PagedListWrapper
import com.guru.bonial.view.news.viewmodel.MainViewModel
import kotlinx.coroutines.CompletableDeferred
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class TestMainViewMode {

    @Rule
    @JvmField
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var newsRepository: INewsRepository

    private lateinit var mainViewModel: MainViewModel

    private lateinit var apiService: MockApiService

    private val mockApiService = Mockito.mock(ApiService::class.java)

    @Before
    fun setup() {
        apiService = MockApiService()
        newsRepository = NewsRepository(mockApiService)
        mainViewModel = Mockito.spy(MainViewModel(newsRepository))
    }

    @Test
    fun testNotEmpty() {
        val pageSize = 21
        val newNews = apiService.createNews(21, startFrom = 21)
        val response = PaginatedResponse<News>(newNews, true, 28)

        val deferred = CompletableDeferred<PaginatedResponse<News>>()
        deferred.complete(response)

        `when`(mockApiService.getTopHeadlines(1, pageSize)).thenReturn(deferred)
        newsRepository = NewsRepository(mockApiService)

        val mainViewModel = MainViewModel(newsRepository)
        mainViewModel.news.observeForever {
            assert(it != null)
            assert(it?.isEmpty() == true)
        }
    }

    @Test
    fun testEmpty() {
        val pageSize = 21
        val response = PaginatedResponse<News>(listOf(), true, 28)

        val deferred = CompletableDeferred<PaginatedResponse<News>>()
        deferred.complete(response)

        `when`(mockApiService.getTopHeadlines(1, pageSize)).thenReturn(deferred)
        newsRepository = NewsRepository(mockApiService)

        val mainViewModel = MainViewModel(newsRepository)
        mainViewModel.news.observeForever {
            assert(it != null)
            assert(it?.isEmpty() == true)
        }
    }
/*

    @Test
    fun testLoadingStateSuccess() {
        val newNews = apiService.createNews(21, startFrom = 21)
        apiService.addNews(1, 40, newNews)

        val response = PaginatedResponse<News>(listOf(), true, 28)

        val deferred = CompletableDeferred<PaginatedResponse<News>>()
        deferred.complete(response)

        `when`(mockApiService.getTopHeadlines(1, 21)).thenReturn(deferred)
        val wrapper = newsRepository.getNews(21)
        val loadingState = getLoadingState(wrapper)!!

        assert(loadingState, instanceOf(LoadingState.SUCCESS::class.java))
    }

    @Test
    fun testLoadingStateError() {
        apiService.failure = true
        apiService.addNews(1, 40, listOf())

        val response = PaginatedResponse<News>(listOf(), true, 28)

        val deferred = CompletableDeferred<PaginatedResponse<News>>()
        deferred.complete(response)

        `when`(mockApiService.getTopHeadlines(1, 21)).thenReturn(deferred)
        val wrapper = newsRepository.getNews(21)
        val loadingState = getLoadingState(wrapper)!!

        assert(loadingState, instanceOf(LoadingState.ERROR::class.java))
    }
*/

    private fun getMessages(listing: PagedListWrapper<NewsDataModel>): List<NewsDataModel> {
        val observer = LoggingObserver<PagedList<NewsDataModel>>()
        listing.pagedList.observeForever(observer)
        Assert.assertNotNull(observer.value)
        return observer.value!!
    }

    private fun getLoadingState(listing: PagedListWrapper<NewsDataModel>): LoadingState? {
        val networkObserver = LoggingObserver<LoadingState>()
        listing.loadingState.observeForever(networkObserver)
        return networkObserver.value
    }

    private class LoggingObserver<T> : Observer<T> {
        var value: T? = null
        override fun onChanged(t: T?) {
            this.value = t
        }
    }

    fun List<News>.toModel(): List<NewsDataModel> {
        return fold(mutableListOf<NewsDataModel>()) { acc, newsTemp ->
            acc.add(newsTemp.toModel())
            acc
        }.toList()
    }
}
