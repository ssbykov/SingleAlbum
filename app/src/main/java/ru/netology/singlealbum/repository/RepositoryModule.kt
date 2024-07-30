package ru.netology.singlealbum.repository

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import ru.netology.singlealbum.controller.MediaPlayerController
import ru.netology.singlealbum.viewmodel.AlbumViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {


    @Provides
    @Singleton
    fun provideOkHttp() = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .build()
}