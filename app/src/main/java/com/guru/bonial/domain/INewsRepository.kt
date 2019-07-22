package com.guru.bonial.domain

import com.guru.bonial.view.news.utils.PagedListWrapper
import com.guru.bonial.view.news.model.NewsDataModel

interface INewsRepository {
    fun getNews(pageSize: Int): PagedListWrapper<NewsDataModel>
}
