package ru.netology.singlealbum.adapter

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object AdapterModule {
    @Provides
    fun provideItemAdapter(): TraksAdapter {
        return TraksAdapter
    }
}