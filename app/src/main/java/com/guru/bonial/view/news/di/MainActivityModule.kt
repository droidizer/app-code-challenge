package com.guru.bonial.view.news.di

import com.guru.bonial.domain.INewsRepository
import com.guru.bonial.view.news.viewmodel.NewsViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule {
    @Provides
    fun providesNewsFactory(repository: INewsRepository) =
        NewsViewModelFactory(repository)
}
