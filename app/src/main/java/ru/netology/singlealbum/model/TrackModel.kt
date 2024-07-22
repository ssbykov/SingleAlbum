package ru.netology.singlealbum.model

import androidx.constraintlayout.widget.ConstraintLayout
import ru.netology.singlealbum.dto.Track

data class TrackModel(
    val track: Track,
    val card: ConstraintLayout
)
