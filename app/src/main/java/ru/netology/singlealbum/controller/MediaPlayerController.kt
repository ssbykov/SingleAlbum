package ru.netology.singlealbum.controller

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import ru.netology.singlealbum.adapter.TrackVieweHolderInteface
import ru.netology.singlealbum.adapter.TraksAdapter
import ru.netology.singlealbum.databinding.SongCardBinding
import ru.netology.singlealbum.dto.Track

private const val BASE_PATH =
    "https://raw.githubusercontent.com/netology-code/andad-homeworks/master/09_multimedia/data/"


class MediaPlayerController private constructor(private val trackAdapter: TraksAdapter) {
    private var mediaPlayer: MediaPlayer? = null
    private var handler: Handler? = null
    private var runnable: Runnable? = null
    private var songCardBinding: SongCardBinding? = null
    private var track: Track? = null

    companion object {
        @Volatile
        private var instance: MediaPlayerController? = null

        fun getInstance(trackAdapter: TraksAdapter): MediaPlayerController {
            return instance ?: synchronized(this) {
                instance ?: MediaPlayerController(trackAdapter).also { instance = it }
            }
        }
    }

    fun pauseOn() {
        mediaPlayer?.pause()
    }

    fun pauseOff() {
        mediaPlayer?.start()
    }

    fun playTrack(newTrack: Track) {
        stopCurrentTrack()
        track = newTrack
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setDataSource(BASE_PATH + newTrack.file)
        mediaPlayer?.prepare()
        mediaPlayer?.start()
        if (track != null) {
            startTimeUpdates(requireNotNull(track))
        }
    }

    fun stopCurrentTrack() {
        mediaPlayer?.stop()
        mediaPlayer?.reset()
        mediaPlayer = null
        if (track != null) {
            stopTimeUpdates(requireNotNull(track))
        }
    }

    fun getMediaPlayer(): MediaPlayer? {
        return mediaPlayer
    }

    private fun startTimeUpdates(newTrack: Track) {
        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                val currentPosition = mediaPlayer?.currentPosition ?: 0
                val duration = mediaPlayer?.duration ?: 0
                trackAdapter.updateItem(newTrack.id.toInt() - 1, newTrack.copy(
                    currentPosition = currentPosition,
                    duration = duration,
                    isPlaying = true
                ))
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

    private fun stopTimeUpdates(track: Track) {
        trackAdapter.updateItem(track.id.toInt() - 1, track)
        handler?.removeCallbacks(requireNotNull(runnable))
        handler = null
        runnable = null
    }
}
