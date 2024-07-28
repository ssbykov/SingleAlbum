package ru.netology.singlealbum.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.singlealbum.R
import ru.netology.singlealbum.controller.MediaPlayerController
import ru.netology.singlealbum.databinding.SongCardBinding
import ru.netology.singlealbum.dto.Track
import ru.netology.singlealbum.utils.fromMillis

class TraksAdapter(
    private val items: MutableList<Track>,
    private val isPaused: Boolean,
    private val mediaPlayerController: MediaPlayerController
) :
    ListAdapter<Track, TrackVieweHolder>(TrackDiffCallback) {
    override fun onBindViewHolder(holder: TrackVieweHolder, position: Int) {
        val track = getItem(position)
        holder.bind(track, isPaused)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackVieweHolder {
        val binding = SongCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrackVieweHolder(binding, this, mediaPlayerController)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return items[position].id.toLong()
    }

}

class TrackVieweHolder(
    private val binding: SongCardBinding,
    private val traksAdapter: TraksAdapter,
    private val mediaPlayerController: MediaPlayerController
) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("ClickableViewAccessibility")
    fun bind(track: Track, isPaused: Boolean) {

        with(binding) {
            trackName.text = track.file
            progress.isEnabled = track.isPlaying
            playTrack.setImageResource(
                if (!isPaused && track.isPlaying) R.drawable.ic_pause_24 else R.drawable.ic_play_arrow_24
            )
            if (track.duration != 0) {
                progress.progress = (track.currentPosition * 100) / track.duration
            }
            time.setText(fromMillis(track.duration))

            playTrack.setOnClickListener {
                if (!track.isPlaying) {
                    mediaPlayerController.playTrack(track, false)
                } else {
                    if (isPaused) {
                        mediaPlayerController.pauseOff()
                    } else {
                        mediaPlayerController.pauseOn()
                    }
                }
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
        }
    }
}

object TrackDiffCallback : DiffUtil.ItemCallback<Track>() {
    override fun areContentsTheSame(oldItem: Track, newItem: Track) =
        oldItem.id == newItem.id

    override fun areItemsTheSame(oldItem: Track, newItem: Track) =
        oldItem == newItem
}
