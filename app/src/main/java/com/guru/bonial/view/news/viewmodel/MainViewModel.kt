package com.guru.bonial.view.news.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.guru.bonial.domain.INewsRepository


class MainViewModel constructor(private val newsRepository: INewsRepository) : ViewModel() {

     val pageSize = MutableLiveData<Int>()
     val newsTransformer = Transformations.map(pageSize) { newsRepository.getNews(it) }

    init {
        pageSize.value = 21
    }

    val news = Transformations.switchMap(newsTransformer) { it.pagedList }!!

   val loadingState = Transformations.switchMap(newsTransformer) { it.loadingState }!!
}
