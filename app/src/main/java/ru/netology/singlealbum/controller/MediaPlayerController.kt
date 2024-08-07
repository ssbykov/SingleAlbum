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
    private var isContinuous = true

    private var onFirstListener: ((Boolean) -> Unit)? = null
    private var onLastListener: ((Boolean) -> Unit)? = null
    private var onPlayListFinishedListener: ((Boolean) -> Unit)? = null


    fun setPlayMode(isChecked: Boolean) {
        isContinuous = isChecked
    }

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
        } else {
            stopCurrentTrack()
            onFirstListener?.invoke(true)
            onLastListener?.invoke(true)
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

    fun getDiffPosition(trackModel: TrackModel): Int {
        val position = playList.indexOf(trackModel)
        return if (position != -1 && playList.size > 1) {
            position - trackIndex
        } else 0
    }

    fun isFirst(): Boolean {
        return trackIndex in (-1..0)
    }

    fun isLast(): Boolean {
        return trackIndex == playList.size - 1
    }

    fun isEmptyPlayList(): Boolean {
        return playList.size == 0
    }


    private fun nextTrack(step: Int) {
        stopCurrentTrack()
        trackIndex += step
        if (trackIndex !in (0..playList.size - 1)) {
            if (isContinuous) {
                trackIndex = if (trackIndex < 0 && step == -1) playList.size - 1 else -1
                nextTrack(if (trackIndex == playList.size - 1) 0 else 1)
            } else {
                songCard = null
                playList = emptyList()
            }
            return
        }
        onFirstListener?.invoke(
            if (!isContinuous) trackIndex == 0 else false
        )
        onLastListener?.invoke(
            if (!isContinuous) trackIndex == playList.size - 1 else false
        )
        playPath = BASE_PATH + playList[trackIndex].track.file
        songCard = playList[trackIndex].card
        songCard?.init()
    }

    private fun playTrack() {
        mediaPlayer = MediaPlayer()
        mediaPlayer?.apply {
            setDataSource(playPath)
            prepare()
            start()
            setOnCompletionListener {
                onPlayListFinishedListener?.invoke(playList.size == trackIndex + 1)
                playNext()
            }
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

    fun setOnFirstListener(listener: (Boolean) -> Unit) {
        onFirstListener = listener
    }

    fun setOnLastListener(listener: (Boolean) -> Unit) {
        onLastListener = listener
    }

    fun setOnPlayListFinishedListener(listener: (Boolean) -> Unit) {
        onPlayListFinishedListener = listener
    }
}
