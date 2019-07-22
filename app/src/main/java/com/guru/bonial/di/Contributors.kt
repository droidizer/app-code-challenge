package com.guru.bonial.di

import com.guru.bonial.view.news.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface Contributors {

    @ContributesAndroidInjector
    fun injectMainActivity(): MainActivity
}
