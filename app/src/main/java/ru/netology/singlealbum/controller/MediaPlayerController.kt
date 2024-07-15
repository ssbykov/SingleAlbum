package ru.netology.singlealbum.controller

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import ru.netology.singlealbum.R
import ru.netology.singlealbum.databinding.SongCardBinding
import ru.netology.singlealbum.utils.fromMillis

private const val BASE_PATH =
    "https://raw.githubusercontent.com/netology-code/andad-homeworks/master/09_multimedia/data/"


class MediaPlayerController() {
    private var mediaPlayer: MediaPlayer? = null
    private var handler: Handler? = null
    private var runnable: Runnable? = null

    fun playTrack(trackPath: String, songCardBinding: SongCardBinding) {
        stopCurrentTrack()
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setDataSource(BASE_PATH + trackPath)
        mediaPlayer?.prepare()
        mediaPlayer?.start()
        songCardBinding.progress.isFocusable = true
        startTimeUpdates(songCardBinding)
    }

    fun stopCurrentTrack() {
        mediaPlayer?.stop()
        mediaPlayer?.reset()
        mediaPlayer = null

        stopTimeUpdates()
    }

    private fun startTimeUpdates(songCardBinding: SongCardBinding) {
        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                val currentPosition = mediaPlayer?.currentPosition ?: 0
                val duration = mediaPlayer?.duration ?: 0
                songCardBinding.progress.progress = (currentPosition * 100) / duration
                val zero = songCardBinding.root.resources.getString(R.string._0_00)
                if (songCardBinding.time.text == zero) {
                    songCardBinding.time.setText(fromMillis(duration))
                }
//                listener.onTimeUpdated(currentPosition, duration)
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

    private fun stopTimeUpdates() {
        handler?.removeCallbacks(requireNotNull(runnable))
        handler = null
        runnable = null
    }
}

interface MediaPlayerListener {
    fun onTimeUpdated(currentPosition: Int, duration: Int)
}