package com.acadia.punk.presentation.binding

import android.view.View
import android.widget.ImageView
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import coil.api.load
import coil.size.Scale
import com.acadia.punk.R

@BindingAdapter("isVisible")
fun setVisible(view: View, value: Boolean?) {
    view.isVisible = value ?: false
}

@BindingAdapter("isInvisible")
fun setInvisible(view: View, value: Boolean?) {
    view.isInvisible = value ?: false
}

@BindingAdapter("isGone")
fun setGone(view: View, value: Boolean?) {
    view.isGone = value ?: false
}

@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, imageUrl: String?) {
    imageView.load(imageUrl) {
        crossfade(true)
        scale(Scale.FILL)
        error(R.drawable.ic_error_outline)
        fallback(R.drawable.ic_error_outline)
    }
}

