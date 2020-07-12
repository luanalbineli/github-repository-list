package com.github.repositorylist.ui

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions

@BindingAdapter(
    value = ["imageUrl", "imageCircular"],
    requireAll = false
)
fun ImageView.imageUrlBinding(
    imageUrl: String?,
    isCircular: Boolean = false
) {
    if (imageUrl == null) {
        return
    }

    var requestManager = Glide.with(this)
        .load(imageUrl)

    if (isCircular) {
        requestManager = requestManager.apply(
            RequestOptions.bitmapTransform(
                MultiTransformation(
                    CircleCrop()
                )
            )
        )
    }

    requestManager.into(this)
}