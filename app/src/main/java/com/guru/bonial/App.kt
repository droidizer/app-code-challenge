package com.guru.bonial

import com.guru.bonial.di.DaggerNewsComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class App: DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerNewsComponent
            .builder()
            .application(this)
            .build()
}
