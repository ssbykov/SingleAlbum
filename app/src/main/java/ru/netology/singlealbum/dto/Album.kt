package ru.netology.singlealbum.dto

import android.view.View
import kotlin.time.Duration

data class Album(
    val id: Long,
    val title: String,
    val subtitle: String,
    val artist: String,
    val published: String,
    val genre: String,
    val tracks: List<Track>,
)

data class Track(
    val id: Long,
    val number: Int?,
    val file: String,
    val duration: Int,
    val isPlaying: Boolean = false,
)