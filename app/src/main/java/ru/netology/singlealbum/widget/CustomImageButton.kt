package ru.netology.singlealbum.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import ru.netology.singlealbum.R

class CustomImageButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.imageButtonStyle
) : AppCompatImageButton(context, attrs, defStyleAttr) {
    private var isChecked = false
    private var onCheckedChangeListener: ((Boolean) -> Unit)? = null
    private var normalIcon = ContextCompat.getDrawable(context, R.drawable.ic_play_arrow_24)
    private var pressedIcon = ContextCompat.getDrawable(context, R.drawable.ic_pause_24)

    init {

        setOnClickListener {
            isChecked = !isChecked
            updateButtonState()
            onCheckedChangeListener?.invoke(isChecked)
        }
    }

    fun setIcons(normalIcon: Int, pressedIcon: Int) {
        this.normalIcon = ContextCompat.getDrawable(context, normalIcon)
        this.pressedIcon = ContextCompat.getDrawable(context, pressedIcon)
    }

    private fun updateButtonState() {
        isPressed = isChecked
        setImageDrawable(if (isPressed) pressedIcon else normalIcon)
    }

    fun setChecked(checked: Boolean) {
        isChecked = checked
        updateButtonState()
    }

    fun setOnCheckedChangeListener(listener: (Boolean) -> Unit) {
        onCheckedChangeListener = listener
    }


}

