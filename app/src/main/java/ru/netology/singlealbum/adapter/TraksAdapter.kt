package ru.netology.singlealbum.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.singlealbum.R
import ru.netology.singlealbum.controller.MediaPlayerController
import ru.netology.singlealbum.controller.PlayMode
import ru.netology.singlealbum.databinding.SongCardBinding
import ru.netology.singlealbum.dto.Track
import ru.netology.singlealbum.utils.fromMillis

class TraksAdapter(
    private val mediaPlayerController: MediaPlayerController
) : ListAdapter<Track, TrackVieweHolder>(TrackDiffCallback) {

    override fun onBindViewHolder(holder: TrackVieweHolder, position: Int) {
        val track = getItem(position)
        holder.bind(track)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackVieweHolder {
        val binding = SongCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrackVieweHolder(binding, mediaPlayerController, getTraks())
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return getTraks()[position].id.toLong()
    }

    private fun getTraks(): List<Track> {
        return mediaPlayerController.viewModel.data.value?.album?.tracks?.toList() ?: emptyList()
    }
}

class TrackVieweHolder(
    private val binding: SongCardBinding,
    private val mediaPlayerController: MediaPlayerController,
    private val tracks: List<Track>
) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("ClickableViewAccessibility")
    fun bind(track: Track) {

        with(binding) {
            trackName.text = track.file
            progress.isEnabled = track.isPlaying
            currentTime.visibility = if (track.isPlaying) VISIBLE else GONE
            currentTime.setText(fromMillis(track.currentPosition))
            playTrack.setImageResource(
                if (!mediaPlayerController.isPaused() && track.isPlaying) R.drawable.ic_pause_24
                else R.drawable.ic_play_arrow_24
            )
            if (track.duration != 0) {
                progress.progress = (track.currentPosition * 100) / track.duration
            }
            time.setText(fromMillis(track.duration))

            playTrack.setOnClickListener {
                if (!mediaPlayerController.isPlaying()) {
                    mediaPlayerController.setPlayMode(PlayMode.SINGLE)
                    mediaPlayerController.playTrack(track)
                } else if (mediaPlayerController.isPlaying() && track.isPlaying) {
                    mediaPlayerController.setPlayPause()
                } else {
                    mediaPlayerController.playTrack(track)
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
