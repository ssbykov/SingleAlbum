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


class MediaPlayerController private constructor(private val adapter: TraksAdapter) {
    private var mediaPlayer: MediaPlayer? = null
    private var handler: Handler? = null
    private var runnable: Runnable? = null
    private var songCardBinding: SongCardBinding? = null
    private var currentTrack: Track? = null
    private var trackVieweHolderInteface: TrackVieweHolderInteface? = null

    companion object {
        @Volatile
        private var instance: MediaPlayerController? = null

        fun initialize(adapter: TraksAdapter) {
            instance ?: synchronized(this) {
                instance ?: MediaPlayerController(adapter).also { instance = it }
            }
        }

        fun getInstance(trackVieweHolderInteface: TrackVieweHolderInteface): MediaPlayerController? {
            instance ?: throw Exception("Контроллер не инициализирован")
            return instance?.setInterface(trackVieweHolderInteface)
        }
    }

    fun setInterface(newInterface: TrackVieweHolderInteface?): MediaPlayerController {
        trackVieweHolderInteface = newInterface
        return this
    }

    fun pauseOn() {
        mediaPlayer?.pause()
    }

    fun pauseOff() {
        mediaPlayer?.start()
    }

    fun playTrack(track: Track) {
        if (trackVieweHolderInteface == null) throw Exception("Интерфейс не установлен")
        currentTrack = track
        stopCurrentTrack()
        songCardBinding = trackVieweHolderInteface?.setNewCard()
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setDataSource(BASE_PATH + track.file)
        mediaPlayer?.prepare()
        mediaPlayer?.start()
        mediaPlayer?.setOnCompletionListener {
            stopCurrentTrack()
        }
        startTimeUpdates()
    }

    private fun stopCurrentTrack() {
        mediaPlayer?.stop()
        mediaPlayer?.reset()
        mediaPlayer = null
        trackVieweHolderInteface?.resetCongCard(songCardBinding)
        stopTimeUpdates()
    }

    private fun startTimeUpdates() {
        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                val currentPosition = mediaPlayer?.currentPosition ?: 0
                val duration = mediaPlayer?.duration ?: 0
                trackVieweHolderInteface?.initNewCard(songCardBinding, currentPosition, duration)
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
