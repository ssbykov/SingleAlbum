package ru.netology.singlealbum.utils

import java.util.Locale


fun fromMillis(duration: Int): String {
    val minutes = duration / 60000
    val seconds = (duration % 60000) / 1000
    return String.format(Locale("ru"),"%d:%02d", minutes, seconds)
}