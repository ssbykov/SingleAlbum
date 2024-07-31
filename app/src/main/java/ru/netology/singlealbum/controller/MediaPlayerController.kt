package ru.netology.singlealbum.controller

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import ru.netology.singlealbum.databinding.SongCardBinding
import ru.netology.singlealbum.dto.Track
import ru.netology.singlealbum.viewmodel.AlbumViewModel

private const val BASE_PATH =
    "https://raw.githubusercontent.com/netology-code/andad-homeworks/master/09_multimedia/data/"


class MediaPlayerController private constructor(
    val viewModel: AlbumViewModel
) {

    private var mediaPlayer: MediaPlayer? = null
    private var handler: Handler? = null
    private var runnable: Runnable? = null
    private var songCardBinding: SongCardBinding? = null
    private var track: Track? = null
    private var playMode: PlayMode? = null

    companion object {
        @Volatile
        private var instance: MediaPlayerController? = null

        fun getInstance(viewModel: AlbumViewModel): MediaPlayerController {
            return instance ?: synchronized(this) {
                instance ?: MediaPlayerController(viewModel).also { instance = it }
            }
        }
    }

    fun isPaused(): Boolean {
        return if (mediaPlayer != null) {
            !requireNotNull(mediaPlayer).isPlaying() &&
                    requireNotNull(mediaPlayer).getCurrentPosition() > 0
        } else false
    }

    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying ?: false
    }

    fun setPlayPause() {
        if (isPaused()) mediaPlayer?.start() else mediaPlayer?.pause()
    }

    fun setPlayMode(playMode: PlayMode?) {
        if (this.playMode != PlayMode.ALL || playMode == null) {
            this.playMode = playMode
        }
    }

    fun playTrack(newTrack: Track) {
        stopCurrentTrack()
        track = newTrack
        mediaPlayer = MediaPlayer()
        mediaPlayer?.apply {
            setDataSource(BASE_PATH + newTrack.file)
            prepare()
            start()
            setOnCompletionListener {
                nextTrack()
            }
        }
        if (track != null) {
            startTimeUpdates()
        }
        viewModel.setIsPlaying(true)
    }

    fun nextTrack(step: Int = 1) {
        val index = (track?.id ?: 0L) - 1 + step
        val tracks = viewModel.albumData.value?.album?.tracks
        if (tracks.isNullOrEmpty()) return
        if (playMode == PlayMode.ALL) {
            val nextTrack = when {
                (index < 0) -> tracks.last()
                (index >= tracks.size) -> tracks[tracks.size - index.toInt()]
                else -> tracks[index.toInt()]
            }
            playTrack(nextTrack)
        } else {
            playTrack(requireNotNull(track))
        }

    }

    fun stopCurrentTrack() {
        mediaPlayer?.stop()
        mediaPlayer?.reset()
        mediaPlayer = null
        if (track != null) {
            stopTimeUpdates()
        }
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
                track = track?.copy(
                    currentPosition = currentPosition,
                    duration = duration,
                    isPlaying = true,
                )
                viewModel.updateItem(requireNotNull(track))
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
        track?.copy(isPlaying = false, currentPosition = 0)?.let { viewModel.updateItem(it) }
        handler?.removeCallbacks(requireNotNull(runnable))
        handler = null
        runnable = null
    }
}

enum class PlayMode {
    SINGLE, ALL
}