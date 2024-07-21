package ru.netology.singlealbum.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.singlealbum.R
import ru.netology.singlealbum.controller.MediaPlayerController
import ru.netology.singlealbum.databinding.SongCardBinding
import ru.netology.singlealbum.dto.Track
import ru.netology.singlealbum.widget.CustomImageButton
import ru.netology.singlealbum.widget.CustomSeekBar
import javax.inject.Inject
import javax.inject.Singleton

object TraksAdapterNew {
    private val _cards = mutableListOf<ConstraintLayout>()
    var mediaPlayerController: MediaPlayerController? = null

    val cards: List<ConstraintLayout>
        get() = _cards

    fun addItem(tracks: List<Track>?, container: LinearLayout) {
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
                    mediaPlayerController = play(isPressed, track, progress, itemView)
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

    private fun play(
        isPressed: Boolean,
        track: Track,
        progress: CustomSeekBar,
        itemView: ConstraintLayout
    ): MediaPlayerController? {
        val trackIntefaceImpl = TrackIntefaceImpl(itemView)
        val mediaPlayerController =
            MediaPlayerController.getInstance(trackIntefaceImpl)
        if (isPressed) {
            if (progress.currentPosition == 0) {
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

