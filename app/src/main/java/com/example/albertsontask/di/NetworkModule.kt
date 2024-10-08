package com.example.albertsontask.di

import com.example.albertsontask.BuildConfig
import com.example.albertsontask.data.api.ApiInterface
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    var retrofit: Retrofit? = null

    @Provides
    @Singleton
    fun requestInterceptor(): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader("Accept", "application/json")
                .build()
            chain.proceed(newRequest)
        }
    }

    @Provides
    @Singleton
    fun provideOkhttp(requestInterceptor: Interceptor): OkHttpClient.Builder {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        httpClient.readTimeout(2, TimeUnit.MINUTES)
        httpClient.writeTimeout(2, TimeUnit.MINUTES)
        httpClient.callTimeout(2, TimeUnit.MINUTES)
        httpClient.addInterceptor(interceptor)
        httpClient.addInterceptor(requestInterceptor)
        return httpClient
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().setLenient().create()
    }


    @Provides
    @Synchronized
    fun provideRetrofit(
        okHttpBuilder: OkHttpClient.Builder,
        gson: Gson,
    ): Retrofit {
        retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpBuilder.build()).build()
        return retrofit!!
    }

    @Provides
    fun provideRestApiService(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }

}