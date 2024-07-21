//package ru.netology.singlealbum.adapter
//
//import android.annotation.SuppressLint
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import android.widget.SeekBar
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.ListAdapter
//import androidx.recyclerview.widget.RecyclerView
//import ru.netology.singlealbum.R
//import ru.netology.singlealbum.controller.MediaPlayerController
//import ru.netology.singlealbum.databinding.SongCardBinding
//import ru.netology.singlealbum.dto.Track
//import javax.inject.Inject
//import javax.inject.Singleton
//
//class TraksAdapter(private var items: MutableList<Track>) :
//    ListAdapter<Track, TrackVieweHolder>(TrackDiffCallback) {
//    override fun onBindViewHolder(holder: TrackVieweHolder, position: Int) {
//        holder.bind(items[position])
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackVieweHolder {
//        val binding = SongCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return TrackVieweHolder(binding)
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        return position
//    }
//
//    fun setItems(tracks: MutableList<Track>) {
//        items = tracks
//    }
//
//    fun updateItem(position: Int, track: Track) {
//        items[position] = track
//        notifyItemChanged(position)
//    }
//
//}
//
//class TrackVieweHolder(
//    private val binding: SongCardBinding,
//) : RecyclerView.ViewHolder(binding.root) {
//    var mediaPlayerController: MediaPlayerController? = null
//
//    @SuppressLint("ClickableViewAccessibility")
//    fun bind(
//        track: Track,
//    ) {
//
//        with(binding) {
//            trackName.text =
//                binding.root.resources.getString(
//                    R.string.track,
//                    track.number.toString(),
//                    track.file
//                )
//            progress.isEnabled = track.isPlaying
//            playTrack.setOnCheckedChangeListener { isPressed ->
//                mediaPlayerController = play(isPressed, track)
//            }
//
//            progress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//                var setProgress: Int? = null
//                override fun onProgressChanged(
//                    seekBar: SeekBar?,
//                    progress: Int,
//                    fromUser: Boolean
//                ) {
//                    setProgress = progress
//                }
//
//                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
//
//                override fun onStopTrackingTouch(seekBar: SeekBar?) {
//                    mediaPlayerController?.updateProgress(setProgress ?: 0)
//                }
//
//            })
//        }
//    }
//}
//
//object TrackDiffCallback : DiffUtil.ItemCallback<Track>() {
//    override fun areContentsTheSame(oldItem: Track, newItem: Track) =
//        oldItem.id == newItem.id
//
//    override fun areItemsTheSame(oldItem: Track, newItem: Track) =
//        oldItem == newItem
//
//}
//
//fun SongCardBinding.play(isPressed: Boolean, track: Track): MediaPlayerController? {
//    val trackVieweHolderIntefaceImpl = TrackVieweHolderIntefaceImpl(this)
//    val mediaPlayerController =
//        MediaPlayerController.getInstance(trackVieweHolderIntefaceImpl)
//    if (isPressed) {
//        if (progress.currentPosition == 0) {
//            mediaPlayerController?.playTrack(track)
//        } else {
//            mediaPlayerController?.pauseOff()
//        }
//    } else {
//        mediaPlayerController?.pauseOn()
//    }
//    return mediaPlayerController
//}
//
//interface TrackVieweHolderInteface {
//    fun setNewCard(): SongCardBinding
//    fun initNewCard(newCard: SongCardBinding?, currentPosition: Int, duration: Int)
//    fun resetCongCard(oldSongCard: SongCardBinding?)
//}