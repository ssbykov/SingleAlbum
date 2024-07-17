package ru.netology.singlealbum.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatSeekBar

class CustomSeekBar: AppCompatSeekBar {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    var currentPosition = 0
    var isFinished = false

}