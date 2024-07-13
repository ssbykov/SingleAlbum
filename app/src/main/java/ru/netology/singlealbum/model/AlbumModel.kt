package ru.netology.singlealbum.model

import ru.netology.singlealbum.dto.Album

data class AlbumModel(
    val album: Album? = null,
    val load: Boolean = false,
    val error: Boolean = false,
)
