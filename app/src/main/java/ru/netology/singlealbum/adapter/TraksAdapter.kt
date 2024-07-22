package ru.netology.singlealbum.adapter

import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import ru.netology.singlealbum.R
import ru.netology.singlealbum.controller.MediaPlayerController
import ru.netology.singlealbum.dto.Track
import ru.netology.singlealbum.model.TrackModel
import ru.netology.singlealbum.widget.CustomImageButton
import ru.netology.singlealbum.widget.CustomSeekBar

object TraksAdapter {
    private val _data = mutableListOf<TrackModel>()
    val data: List<TrackModel>
        get() = _data

    var mediaPlayerController: MediaPlayerController? = null

    fun addItem(tracks: List<Track>?, container: LinearLayout) {
        tracks?.let {
            it.forEach { track ->
                val inflater = LayoutInflater.from(container.context)
                val itemView =
                    inflater.inflate(R.layout.song_card, container, false) as ConstraintLayout
                val trackNameView = itemView.findViewById<TextView>(R.id.trackName)
                val trackModel = TrackModel(track, itemView)
                trackNameView.text = track.file
                val progress = itemView.findViewById<CustomSeekBar>(R.id.progress)
                progress.isEnabled = track.isPlaying
                val playTrack = itemView.findViewById<CustomImageButton>(R.id.playTrack)
                playTrack.setOnCheckedChangeListener { isPressed ->
                    mediaPlayerController = play(trackModel, isPressed)
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
                        mediaPlayerController?.updateProgress(setProgress ?: 0)
                    }

                })

                container.addView(itemView)
                _data.add(trackModel)
            }
        }
    }

    fun play(
        trackModel: TrackModel,
        isPressed: Boolean = true,
    ): MediaPlayerController? {
        val trackIntefaceImpl = TrackIntefaceImpl(trackModel.card)
        val mediaPlayerController =
            MediaPlayerController.getInstance(trackIntefaceImpl)
        if (isPressed) {
            val progress = trackModel.card.findViewById<CustomSeekBar>(R.id.progress)
            val playTrack = trackModel.card.findViewById<CustomImageButton>(R.id.playTrack)
            if (progress.currentPosition == 0) {
                playTrack.setChecked(true)
                mediaPlayerController?.playTrack(trackModel.track)
            } else {
                mediaPlayerController?.pauseOff()
            }
        } else {
            mediaPlayerController?.pauseOn()
        }
        return mediaPlayerController
    }
}

interface TrackInteface {
    fun setNewCard(): ConstraintLayout
    fun initNewCard(newCard: ConstraintLayout?, currentPosition: Int, duration: Int)
    fun resetCongCard(oldCard: ConstraintLayout?)
}

