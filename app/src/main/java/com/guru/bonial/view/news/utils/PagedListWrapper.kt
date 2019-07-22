package com.guru.bonial.view.news.utils

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

data class PagedListWrapper<T>(
    val pagedList: LiveData<PagedList<T>>,
    val loadingState: LiveData<LoadingState>,
    val load: () -> Unit
)
