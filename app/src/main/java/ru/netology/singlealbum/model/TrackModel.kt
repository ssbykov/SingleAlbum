package ru.netology.singlealbum.model

import ru.netology.singlealbum.databinding.SongCardBinding
import ru.netology.singlealbum.dto.Track

data class TrackModel(
    val track: Track,
    val card: SongCardBinding
)
