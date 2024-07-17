package ru.netology.singlealbum

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageButton

class CustomImageButton : AppCompatImageButton {
    private var isChecked = false
    private var onCheckedChangeListener: ((Boolean) -> Unit)? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setOnClickListener {
            isChecked = !isChecked
            updateButtonState()
            onCheckedChangeListener?.invoke(isChecked)
        }
    }

    private fun updateButtonState() {
        isPressed = isChecked
        setImageResource(
            if (isChecked) R.drawable.ic_pause_24 else R.drawable.ic_play_arrow_24
        )
    }

    fun setChecked(checked: Boolean) {
        isChecked = checked
        updateButtonState()
    }

    fun setOnCheckedChangeListener(listener: (Boolean) -> Unit) {
        onCheckedChangeListener = listener
    }
}

