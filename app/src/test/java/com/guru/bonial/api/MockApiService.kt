package com.guru.bonial.api

import com.guru.bonial.models.News
import com.guru.bonial.models.PaginatedResponse
import com.guru.bonial.network.ApiService
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.io.IOException

class MockApiService : ApiService {
    private val model = mutableMapOf<Int, PaginatedResponse<News>>()

    val pageSize: Int = 21
    var failure: Boolean = false
    var lastPage: Boolean = false

    fun addNews(page: Int, total: Int, messages: List<News>): PaginatedResponse<News> {
        val paginatedResponse = model.getOrPut(page) {
            PaginatedResponse(status = true, totalResult = total, articles = messages)
        }
        paginatedResponse.articles.toMutableList().addAll(messages)
        return paginatedResponse
    }

    fun createNews(no: Int, startFrom: Int = 0): List<News> {
        val newsList = mutableListOf<News>()
        for (i in startFrom until no + startFrom) {
            val news = News(
                author = "author$i",
                url = "url$i",
                content = "content$i",
                title = "title$i",
                description = "description$i",
                publishedAt = "date$i",
                urlToImage = "url$i"
            )
            newsList.add(news)
        }
        return newsList.toList()
    }

    override fun getTopHeadlines(page: Int, pageSize: Int): Deferred<PaginatedResponse<News>> {
        if (failure) {
            return GlobalScope.async {
                throw IOException()
            }
        }
        return GlobalScope.async {
            model[page]!!
        }
    }

}
