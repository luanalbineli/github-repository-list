package com.github.repositorylist.extensions

import android.view.View

fun View.setDisplay(show: Boolean) {
    visibility = if (show) View.VISIBLE else View.GONE
    requestLayout()
}

fun View.setVisibility(show: Boolean) {
    visibility = if (show) View.VISIBLE else View.INVISIBLE
    requestLayout()
}