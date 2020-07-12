package com.github.repositorylist.widgets.recyclerView

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class CustomVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val context: Context = itemView.context
}