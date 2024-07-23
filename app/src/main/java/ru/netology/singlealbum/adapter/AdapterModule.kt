package ru.netology.singlealbum.adapter

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import ru.netology.singlealbum.controller.MediaPlayerController

@Module
@InstallIn(ActivityComponent::class)
object AdapterModule {
    @Provides
    fun provideItemAdapter(mediaPlayerController: MediaPlayerController): TraksAdapter {
        return TraksAdapter(mediaPlayerController)
    }
}