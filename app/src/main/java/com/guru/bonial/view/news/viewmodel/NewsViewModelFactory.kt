package com.guru.bonial.view.news.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.guru.bonial.domain.INewsRepository
import javax.inject.Inject

class NewsViewModelFactory @Inject constructor(private val repository: INewsRepository)
    : ViewModelProvider.Factory  {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            MainViewModel(repository) as T
}
