package ru.netology.singlealbum.controller

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import ru.netology.singlealbum.adapter.TrackVieweHolderInteface
import ru.netology.singlealbum.databinding.SongCardBinding

private const val BASE_PATH =
    "https://raw.githubusercontent.com/netology-code/andad-homeworks/master/09_multimedia/data/"


class MediaPlayerController private constructor(private var trackVieweHolderInteface: TrackVieweHolderInteface) {
    private var mediaPlayer: MediaPlayer? = null
    private var handler: Handler? = null
    private var runnable: Runnable? = null
    private var songCardBinding: SongCardBinding? = null

    companion object {
        @Volatile
        private var instance: MediaPlayerController? = null

        fun getInstance(trackVieweHolderInteface: TrackVieweHolderInteface): MediaPlayerController {
            return instance?.setInterface(trackVieweHolderInteface) ?: synchronized(this) {
                instance ?: MediaPlayerController(trackVieweHolderInteface).also { instance = it }
            }
        }
    }

    private fun setInterface(newInterface: TrackVieweHolderInteface): MediaPlayerController {
        this.trackVieweHolderInteface = newInterface
        return this
    }

    fun pauseOn() {
        mediaPlayer?.pause()
    }

    fun pauseOff() {
        mediaPlayer?.start()
    }

    fun playTrack(trackPath: String) {
        stopCurrentTrack()
        songCardBinding = trackVieweHolderInteface.setNewCard()
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setDataSource(BASE_PATH + trackPath)
        mediaPlayer?.prepare()
        mediaPlayer?.start()
        startTimeUpdates()
    }

    fun stopCurrentTrack() {
        mediaPlayer?.stop()
        mediaPlayer?.reset()
        mediaPlayer = null
        stopTimeUpdates(trackVieweHolderInteface.setNewCard())
    }

    fun getMediaPlayer(): MediaPlayer? {
        return mediaPlayer
    }

    private fun startTimeUpdates() {
        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                val currentPosition = mediaPlayer?.currentPosition ?: 0
                val duration = mediaPlayer?.duration ?: 0
                trackVieweHolderInteface.initNewCard(songCardBinding, currentPosition, duration)
                handler?.postDelayed(this, 1000)
            }
        }
        handler?.post(requireNotNull(runnable))
    }

    fun updateProgress(progress: Int) {
        mediaPlayer?.let {
            it.seekTo(progress * it.duration / 100)
        }
    }

    private fun stopTimeUpdates(newCardBinding: SongCardBinding) {
        trackVieweHolderInteface.resetCongCard(songCardBinding)
        songCardBinding?.let {
            if (newCardBinding != it || it.progress.isFinished) {
                it.progress.progress = 0
                it.progress.isFinished = false
                it.progress.currentPosition = 0
                it.playTrack.setChecked(false)
                it.progress.isEnabled = false
            }
        }
        handler?.removeCallbacks(requireNotNull(runnable))
        handler = null
        runnable = null
    }
}
