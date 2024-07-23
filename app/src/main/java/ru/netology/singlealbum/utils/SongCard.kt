package ru.netology.singlealbum.utils

import ru.netology.singlealbum.databinding.SongCardBinding


fun SongCardBinding.update(currentPosition: Int, duration: Int) {
    this.apply {
        progress.currentPosition = currentPosition
        progress.progress = (currentPosition * 100) / duration
        progress.isEnabled = true
        if (time.text == "0:00") {
            time.setText(fromMillis(duration))
        }
    }
}

fun SongCardBinding.reset() {
    this.apply {
        progress.currentPosition = 0
        progress.isEnabled = false
        progress.progress = 0
        playTrack.setChecked(false)
    }
}

fun SongCardBinding.init(): SongCardBinding {
    this.playTrack.isEnabled = true
    this.playTrack.setChecked(true)
    return this
}

fun SongCardBinding.onPause() {
    playTrack.setChecked(false)
}
