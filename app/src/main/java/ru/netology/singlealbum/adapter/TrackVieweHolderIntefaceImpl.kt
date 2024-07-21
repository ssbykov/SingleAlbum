package ru.netology.singlealbum.adapter

import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.constraintlayout.widget.ConstraintLayout
import ru.netology.singlealbum.R
import ru.netology.singlealbum.controller.MediaPlayerController
import ru.netology.singlealbum.databinding.SongCardBinding
import ru.netology.singlealbum.utils.fromMillis
import ru.netology.singlealbum.widget.CustomImageButton
import ru.netology.singlealbum.widget.CustomSeekBar

class TrackIntefaceImpl(
    private val itemView: ConstraintLayout,
) : TrackInteface {

    override fun setNewCard(): ConstraintLayout {
        return itemView
    }

    override fun initNewCard(
        newCard: ConstraintLayout?,
        currentPosition: Int,
        duration: Int
    ) {
        val progress = newCard?.findViewById<CustomSeekBar>(R.id.progress)
        progress?.apply {
            progress.currentPosition = currentPosition
            progress.progress = (currentPosition * 100) / duration
            progress.isEnabled = true
        }
        val timeTextView = newCard?.findViewById<TextView>(R.id.time)
        if (timeTextView?.text == "0:00") {
            timeTextView.setText(fromMillis(duration))
        }
    }

    override fun resetCongCard(oldCard: ConstraintLayout?) {
        val progressSeekBar = oldCard?.findViewById<CustomSeekBar>(R.id.progress)
        progressSeekBar?.apply {
            currentPosition = 0
            isEnabled = false
            progress = 0
            isFinished = false
        }
        val playTrack = oldCard?.findViewById<CustomImageButton>(R.id.playTrack)
        playTrack?.setChecked(false)
    }


}