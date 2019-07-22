package com.guru.bonial.di

import com.guru.bonial.App
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    (Binds::class),
    (Contributors::class),
    (NetworkModule::class),
    (ProvidesModule::class),
    (AndroidSupportInjectionModule::class)])
interface NewsComponent: AndroidInjector<App> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: App): Builder

        fun build(): NewsComponent
    }
}
