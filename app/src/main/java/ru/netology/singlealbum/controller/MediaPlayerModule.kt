package ru.netology.singlealbum.controller

import android.widget.Adapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.netology.singlealbum.adapter.TraksAdapter
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class MediaPlayerModule {

    @Provides
    @Singleton
    fun providerMediaPlayerController(): MediaPlayerController {
        return MediaPlayerController()
    }
}