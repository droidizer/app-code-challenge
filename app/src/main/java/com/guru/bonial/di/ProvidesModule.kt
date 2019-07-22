package com.guru.bonial.di

import com.guru.bonial.App
import dagger.Module
import dagger.Provides

@Module
class ProvidesModule {

    @Provides
    fun provideContext(app: App) = app.applicationContext
}
