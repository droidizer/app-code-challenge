package com.guru.bonial.network

import com.guru.bonial.models.News
import com.guru.bonial.models.PaginatedResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("top-headlines?country=de")
    fun getTopHeadlines(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int): Deferred<PaginatedResponse<News>>
}
