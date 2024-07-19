//package ru.netology.singlealbum.adapter
//
//import ru.netology.singlealbum.R
//import ru.netology.singlealbum.controller.MediaPlayerController
//import ru.netology.singlealbum.databinding.SongCardBinding
//import ru.netology.singlealbum.utils.fromMillis
//
//class TrackVieweHolderIntefaceImpl(
//    private val binding: SongCardBinding,
//) : TrackVieweHolderInteface {
//    val mediaPlayerController = MediaPlayerController.getInstance(this)
//
//    override fun setNewCard(): SongCardBinding {
//        return binding
//    }
//
//    override fun initNewCard(
//        newCard: SongCardBinding?,
//        currentPosition: Int,
//        duration: Int
//    ) {
//        newCard?.let {
//            it.progress.currentPosition = currentPosition
//            it.progress.progress = (currentPosition * 100) / duration
//            it.progress.isEnabled = true
//            val zero = it.root.resources.getString(R.string._0_00)
//            if (it.time.text == zero) {
//                it.time.setText(fromMillis(duration))
//            }
//            mediaPlayerController.getMediaPlayer()?.setOnCompletionListener {
//                newCard.progress.isFinished = true
//                mediaPlayerController.stopCurrentTrack()
//            }
//        }
//    }
//
//    override fun resetCongCard(oldSongCard: SongCardBinding?) {
//        oldSongCard?.let {
//            it.progress.progress = 0
//            it.progress.isEnabled = false
//        }
//    }
//
//
//}