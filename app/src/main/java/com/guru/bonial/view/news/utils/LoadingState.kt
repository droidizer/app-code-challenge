package com.guru.bonial.view.news.utils

sealed class LoadingState {
    object LOADING: LoadingState()
    object SUCCESS: LoadingState()
    object ERROR: LoadingState()
}
