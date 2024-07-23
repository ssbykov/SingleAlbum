package ru.netology.singlealbum.controller

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import ru.netology.singlealbum.databinding.SongCardBinding
import ru.netology.singlealbum.model.TrackModel
import ru.netology.singlealbum.utils.init
import ru.netology.singlealbum.utils.onPause
import ru.netology.singlealbum.utils.reset
import ru.netology.singlealbum.utils.update


private const val BASE_PATH =
    "https://raw.githubusercontent.com/netology-code/andad-homeworks/master/09_multimedia/data/"

class MediaPlayerController() {
    private var mediaPlayer: MediaPlayer? = null
    private var handler: Handler? = null
    private var runnable: Runnable? = null
    private var songCard: SongCardBinding? = null
    private var trackIndex = -1
    private var playPath = ""
    private var playList: List<TrackModel> = emptyList()


    fun pauseOn() {
        mediaPlayer?.pause()
        songCard?.onPause()
    }

    fun pauseOff() {
        mediaPlayer?.start()
    }

    fun play(playList: List<TrackModel>) {
        this.playList = playList
        trackIndex = -1
        if (playList.isNotEmpty()) {
            nextTrack(1)
            playTrack()
        }
    }

    fun playNext(step: Int = 1) {
        nextTrack(step)
        if (songCard != null) {
            playTrack()
        } else {
            stopCurrentTrack()
        }
    }

    private fun nextTrack(step: Int) {
        stopCurrentTrack()
        trackIndex += step
        if (trackIndex in (0..playList.size - 1)) {
            playPath = BASE_PATH + playList[trackIndex].track.file
            songCard = playList[trackIndex].card
            songCard?.init()
        } else songCard = null
    }

    private fun playTrack() {
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setDataSource(playPath)
        mediaPlayer?.prepare()
        mediaPlayer?.start()
        mediaPlayer?.setOnCompletionListener {
            playNext()
        }
        startTimeUpdates()
    }

    fun stopCurrentTrack() {
        mediaPlayer?.stop()
        mediaPlayer?.reset()
        mediaPlayer = null
        songCard?.reset()
        stopTimeUpdates()
    }

    private fun startTimeUpdates() {
        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                val currentPosition = mediaPlayer?.currentPosition ?: 0
                val duration = mediaPlayer?.duration ?: 0
                songCard?.update(currentPosition, duration)
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
