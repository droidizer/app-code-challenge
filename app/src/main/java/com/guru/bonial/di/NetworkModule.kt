package com.guru.bonial.di

import com.guru.bonial.BuildConfig
import com.guru.bonial.network.ApiService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

private const val CONNECTION_TIMEOUT_SECS: Long = 20

@Module
class NetworkModule {

    val BASE_URL = "https://newsapi.org/v2/"

    @Provides
    @Singleton
    fun provideGsonConverterLibrary(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    fun providesCoroutineAdapter(): CoroutineCallAdapterFactory = CoroutineCallAdapterFactory()

    @Singleton
    @Provides
    fun provideOkHttpClient(appInterceptor: AppInterceptor): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            okHttpClientBuilder.addInterceptor(logging)
        }

        okHttpClientBuilder.addInterceptor(appInterceptor)

        okHttpClientBuilder
            .connectTimeout(CONNECTION_TIMEOUT_SECS, TimeUnit.SECONDS)
            .readTimeout(CONNECTION_TIMEOUT_SECS, TimeUnit.SECONDS)
            .writeTimeout(CONNECTION_TIMEOUT_SECS, TimeUnit.SECONDS)

        return okHttpClientBuilder.build()
    }

    @Provides
    @Singleton
    fun provideApiService(
        okHttpClient: OkHttpClient,
        adapter: CoroutineCallAdapterFactory,
        gsonConverterFactory: GsonConverterFactory
    ): ApiService {
        val builder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(adapter)
            .addConverterFactory(gsonConverterFactory)
        val retrofit = builder.client(okHttpClient).build()
        return retrofit.create(ApiService::class.java)
    }

    class AppInterceptor @Inject constructor() : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val original = chain.request()
            val originalHttpUrl = original.url

            val url = originalHttpUrl.newBuilder()
                .addQueryParameter("apiKey", "572c740c76304449a3e9998b9f84bb21")
                .build()

            val requestBuilder = original.newBuilder()
                .url(url)

            val request = requestBuilder.build()
            return chain.proceed(request)
        }
    }
}
