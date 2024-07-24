package ru.netology.singlealbum.utils

import android.widget.ImageButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import ru.netology.singlealbum.R

fun setColorState(imageButton: ImageButton, state: Boolean) {
    imageButton.background = ContextCompat.getDrawable(
        imageButton.context,
        if (state) R.drawable.roundcorner_gray else R.drawable.roundcorner_yellow
    )
    imageButton.isEnabled = !state
}