package ru.netology.singlealbum.adapter

import android.view.LayoutInflater
import android.widget.SeekBar
import ru.netology.singlealbum.R
import ru.netology.singlealbum.controller.MediaPlayerController
import ru.netology.singlealbum.databinding.SongCardBinding
import ru.netology.singlealbum.dto.Track
import ru.netology.singlealbum.model.TrackModel
import javax.inject.Inject

class TraksAdapter @Inject constructor(
    val mediaPlayerController: MediaPlayerController
) {
    private val _data = mutableListOf<TrackModel>()
    val data: List<TrackModel>
        get() = _data

    fun addItem(tracks: List<Track>?, layoutInflater: LayoutInflater) {
        tracks?.let {
            it.forEach { track ->
                val itemBinding = SongCardBinding.inflate(layoutInflater)
                itemBinding.apply {
                    trackName.text =
                        itemBinding.root.resources.getString(R.string.track, track.id, track.file)
                    progress.isEnabled = track.isPlaying
                    val trackModel = TrackModel(track, itemBinding)
                    playTrack.setOnCheckedChangeListener { isPressed ->
                        play(trackModel, isPressed)
                    }
                    progress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                        var setProgress: Int? = null
                        override fun onProgressChanged(
                            seekBar: SeekBar?,
                            progress: Int,
                            fromUser: Boolean
                        ) {
                            setProgress = progress
                        }

                        override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                        override fun onStopTrackingTouch(seekBar: SeekBar?) {
                            mediaPlayerController.updateProgress(setProgress ?: 0)
                        }

                    })
                    _data.add(trackModel)
                }
            }
        }
    }

    private fun play(
        trackModel: TrackModel,
        isPressed: Boolean = true,
    ) {
        if (isPressed) {
            trackModel.card.apply {
                if (progress.currentPosition == 0) {
                    mediaPlayerController.play(listOf(trackModel))
                } else {
                    mediaPlayerController.pauseOff()
                }
            }
        } else {
            mediaPlayerController.pauseOn()
        }
    }
}

