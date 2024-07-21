//package ru.netology.singlealbum.controller
//
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.components.ActivityComponent
//import dagger.hilt.components.SingletonComponent
//import ru.netology.singlealbum.adapter.TrackVieweHolderInteface
//import ru.netology.singlealbum.adapter.TrackVieweHolderIntefaceImpl
//import ru.netology.singlealbum.databinding.SongCardBinding
//
//@Module
//@InstallIn(SingletonComponent::class)
//object MediaPlayerModule {
//
//    @Provides
//    fun providerTrackVieweHolderInteface(binding: SongCardBinding): TrackVieweHolderIntefaceImpl {
//        return TrackVieweHolderIntefaceImpl(binding)
//    }
//
//    @Provides
//    fun providerMediaPlayerController(trackVieweHolderInteface: TrackVieweHolderInteface):
//            MediaPlayerController {
//        return MediaPlayerController.getInstance(trackVieweHolderInteface)
//    }
//}