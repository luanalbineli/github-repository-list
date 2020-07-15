package com.github.repositorylist.ui

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.github.repositorylist.extensions.setDisplay
import com.github.repositorylist.model.common.Status
import com.github.repositorylist.widgets.recyclerView.RequestStatusView

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

@BindingAdapter("goneUnless")
fun View.goneUnless(visible: Boolean) {
    setDisplay(visible)
}

@BindingAdapter("resultStatus")
fun RequestStatusView.resultStatus(status: Status?) {
    if (status == Status.LOADING) {
        toggleStatus(RequestStatusView.RequestStatusType.LOADING)
    } else if (status == Status.ERROR) {
        toggleStatus(RequestStatusView.RequestStatusType.ERROR)
    }
}