package ru.netology.singlealbum.adapter

import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import ru.netology.singlealbum.R
import ru.netology.singlealbum.controller.MediaPlayerController
import ru.netology.singlealbum.dto.Track
import ru.netology.singlealbum.widget.CustomImageButton
import ru.netology.singlealbum.widget.CustomSeekBar

object TraksAdapter {
    private val _cards = mutableListOf<ConstraintLayout>()
    val cards: List<ConstraintLayout>
        get() = _cards

    private val _tracks = mutableListOf<Track>()
    val tracks: List<Track>
        get() = _tracks

    var mediaPlayerController: MediaPlayerController? = null

    fun addItem(tracks: List<Track>?, container: LinearLayout) {
        if (tracks != null) {
            this._tracks.addAll(tracks)
        }
        tracks?.let {
            it.forEach { track ->
                val inflater = LayoutInflater.from(container.context)
                val itemView =
                    inflater.inflate(R.layout.song_card, container, false) as ConstraintLayout
                val trackNameView = itemView.findViewById<TextView>(R.id.trackName)
                trackNameView.text = track.file
                val progress = itemView.findViewById<CustomSeekBar>(R.id.progress)
                progress.isEnabled = track.isPlaying
                val playTrack = itemView.findViewById<CustomImageButton>(R.id.playTrack)
                playTrack.setOnCheckedChangeListener { isPressed ->
                    mediaPlayerController = play(isPressed, track, itemView)
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
                _cards.add(itemView)
            }
        }
    }

    fun play(
        isPressed: Boolean,
        track: Track,
        itemView: ConstraintLayout
    ): MediaPlayerController? {
        val trackIntefaceImpl = TrackIntefaceImpl(itemView)
        val mediaPlayerController =
            MediaPlayerController.getInstance(trackIntefaceImpl)
        if (isPressed) {
            val progress = itemView.findViewById<CustomSeekBar>(R.id.progress)
            val playTrack = itemView.findViewById<CustomImageButton>(R.id.playTrack)
            if (progress.currentPosition == 0) {
                playTrack.setChecked(true)
                mediaPlayerController?.playTrack(track)
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

