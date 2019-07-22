package com.guru.bonial.models

data class PaginatedResponse<T>(val articles: List<T>, val status: Boolean, val totalResult: Int)
