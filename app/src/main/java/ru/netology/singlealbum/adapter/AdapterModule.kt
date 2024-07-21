package ru.netology.singlealbum.adapter

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import ru.netology.singlealbum.viewmodel.AlbumViewModel

@Module
@InstallIn(ActivityComponent::class)
object AdapterModule {
    @Provides
    fun provideItemAdapter(): TraksAdapterNew {
        return TraksAdapterNew
    }
}