package com.guru.bonial.di

import com.guru.bonial.domain.INewsRepository
import com.guru.bonial.domain.NewsRepository
import dagger.Binds
import dagger.Module

@Module
interface Binds {

    @Binds
    fun bindsNewsRepository(newsRepository: NewsRepository): INewsRepository
}
