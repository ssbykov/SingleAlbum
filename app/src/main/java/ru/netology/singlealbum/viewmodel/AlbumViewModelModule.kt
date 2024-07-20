package ru.netology.singlealbum.viewmodel

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import ru.netology.singlealbum.repository.AlbumRepository

@Module
@InstallIn(ViewModelComponent::class)
object AlbumViewModelModule {
    @Provides
    @ViewModelScoped
    fun provideAlbumViewModel(application: Application): AlbumViewModel {
        return AlbumViewModel(application)
    }
}