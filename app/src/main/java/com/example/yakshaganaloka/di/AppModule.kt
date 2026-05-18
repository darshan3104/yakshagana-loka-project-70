package com.example.yakshaganaloka.di

import android.content.Context
import com.example.yakshaganaloka.data.local.LocalJsonDataSource
import com.example.yakshaganaloka.data.repository.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.MediaType.Companion.toMediaType
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = Firebase.firestore

    @Provides
    @Singleton
    fun provideLocalJsonDataSource(@ApplicationContext context: Context): LocalJsonDataSource {
        return LocalJsonDataSource(context)
    }

    @Provides
    @Singleton
    fun providePerformanceRepository(
        localJsonDataSource: LocalJsonDataSource
    ): PerformanceRepository {
        return PerformanceRepository(localJsonDataSource)
    }

    @Provides
    @Singleton
    fun provideArtistRepository(
        localJsonDataSource: LocalJsonDataSource
    ): ArtistRepository {
        return ArtistRepository(localJsonDataSource)
    }

    @Provides
    @Singleton
    fun provideMelaRepository(
        localJsonDataSource: LocalJsonDataSource
    ): MelaRepository {
        return MelaRepository(localJsonDataSource)
    }

    @Provides
    @Singleton
    fun provideAudioRepository(
        localJsonDataSource: LocalJsonDataSource
    ): AudioRepository {
        return AudioRepository(localJsonDataSource)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): okhttp3.OkHttpClient {
        val logging = okhttp3.logging.HttpLoggingInterceptor().apply {
            level = okhttp3.logging.HttpLoggingInterceptor.Level.BODY
        }
        return okhttp3.OkHttpClient.Builder()
            .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${com.example.yakshaganaloka.BuildConfig.OPENROUTER_API_KEY}")
                    .addHeader("HTTP-Referer", "yakshaganaloka.app")
                    .addHeader("X-Title", "Yakshagana Loka")
                    .addHeader("Content-Type", "application/json")
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: okhttp3.OkHttpClient): retrofit2.Retrofit {
        val json = kotlinx.serialization.json.Json { ignoreUnknownKeys = true }
        return retrofit2.Retrofit.Builder()
            .baseUrl("https://openrouter.ai/api/v1/")
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    fun provideOpenRouterApiService(retrofit: retrofit2.Retrofit): com.example.yakshaganaloka.data.remote.OpenRouterApiService {
        return retrofit.create(com.example.yakshaganaloka.data.remote.OpenRouterApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideOpenRouterRepository(
        openRouterApiService: com.example.yakshaganaloka.data.remote.OpenRouterApiService
    ): OpenRouterRepository {
        return OpenRouterRepository(openRouterApiService)
    }
}
