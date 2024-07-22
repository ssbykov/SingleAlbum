package ru.netology.singlealbum.controller

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.constraintlayout.widget.ConstraintLayout
import ru.netology.singlealbum.adapter.TrackInteface
import ru.netology.singlealbum.adapter.TraksAdapter
import ru.netology.singlealbum.dto.Track

private const val BASE_PATH =
    "https://raw.githubusercontent.com/netology-code/andad-homeworks/master/09_multimedia/data/"
private const val NOT_INIT = "The controller is not initialized"
private const val NOT_INST = "The interface is not installed"

class MediaPlayerController private constructor(private val adapter: TraksAdapter) {
    private var mediaPlayer: MediaPlayer? = null
    private var handler: Handler? = null
    private var runnable: Runnable? = null
    private var songCard: ConstraintLayout? = null
    private var trackInteface: TrackInteface? = null

    companion object {
        @Volatile
        private var instance: MediaPlayerController? = null

        fun initialize(adapter: TraksAdapter) {
            instance ?: synchronized(this) {
                instance ?: MediaPlayerController(adapter).also { instance = it }
            }
        }

        fun getInstance(trackInteface: TrackInteface): MediaPlayerController? {
            instance ?: throw Exception(NOT_INIT)
            return instance?.setInterface(trackInteface)
        }
    }

    fun setInterface(newInterface: TrackInteface?): MediaPlayerController {
        trackInteface = newInterface
        return this
    }

    fun pauseOn() {
        mediaPlayer?.pause()
    }

    fun pauseOff() {
        mediaPlayer?.start()
    }

    fun playTrack(track: Track) {
        if (trackInteface == null) throw Exception(NOT_INST)
        stopCurrentTrack()
        songCard = trackInteface?.setNewCard()
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setDataSource(BASE_PATH + track.file)
        mediaPlayer?.prepare()
        mediaPlayer?.start()
        mediaPlayer?.setOnCompletionListener {
            if (track.number in (0..adapter.data.size - 1)) {
                adapter.play(adapter.data[track.number])
            } else {
                stopCurrentTrack()
            }
        }
        startTimeUpdates()
    }

    private fun stopCurrentTrack() {
        mediaPlayer?.stop()
        mediaPlayer?.reset()
        mediaPlayer = null
        trackInteface?.resetCongCard(songCard)
        stopTimeUpdates()
    }

    private fun startTimeUpdates() {
        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                val currentPosition = mediaPlayer?.currentPosition ?: 0
                val duration = mediaPlayer?.duration ?: 0
                trackInteface?.initNewCard(songCard, currentPosition, duration)
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
