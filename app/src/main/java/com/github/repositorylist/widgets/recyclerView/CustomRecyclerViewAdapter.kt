package com.github.repositorylist.widgets.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.repositorylist.R
import timber.log.Timber

abstract class CustomRecyclerViewAdapter<TModel, THolder: CustomVH>
constructor(
    diffCallback: DiffUtil.ItemCallback<TModel>
): ListAdapter<TModel, CustomVH>(diffCallback) {
    var onItemClick: ((adapterPosition: Int, model: TModel) -> Unit)? = null
    var onTryAgain: (() -> Unit)? = null
    var errorMessageResId: Int? = null
    var emptyMessageResId: Int? = null

    private var mItems = emptyList<TModel>()
    val items: List<TModel>
        get() = mItems

    private var mRequestRequestStatusType: RequestStatusView.RequestStatusType? = null

    private var mRequestErrorModel: Exception? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomVH {
        if (viewType == ViewType.GRID_STATUS.ordinal) {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_request_status, parent, false)
            return RequestStatusVH(itemView)
        }

        val layoutVH = onCreateItemViewHolder(LayoutInflater.from(parent.context), parent, viewType)
        layoutVH.itemView.setOnClickListener {
            if (layoutVH.adapterPosition != RecyclerView.NO_POSITION) {
                onItemClick?.invoke(layoutVH.adapterPosition, getItem(layoutVH.adapterPosition))
            }
        }

        return layoutVH
    }

    override fun onBindViewHolder(holder: CustomVH, position: Int) {
        if (holder.itemViewType == ViewType.GRID_STATUS.ordinal) {
            Timber.d("Redrawing the request status: $mRequestRequestStatusType - position: $position")
            val requestStatusVH = holder as RequestStatusVH
            requestStatusVH.bind(mRequestRequestStatusType!!, items.isEmpty(), errorMessageResId, emptyMessageResId, onTryAgain)
            return
        }

        @Suppress("UNCHECKED_CAST")
        onBindItemViewHolder(holder as THolder, position)
    }

    final override fun getItemViewType(position: Int): Int {
        if (position == super.getItemCount()) {
            return ViewType.GRID_STATUS.ordinal
        }
        return ViewType.ITEM.ordinal
    }

    fun showLoadingStatus() {
        redrawGridStatus(RequestStatusView.RequestStatusType.LOADING)
    }

    fun showErrorStatus(exception: Exception? = null) {
        mRequestErrorModel = exception
        redrawGridStatus(RequestStatusView.RequestStatusType.ERROR)
    }

    fun showEmptyStatus() {
        redrawGridStatus(RequestStatusView.RequestStatusType.EMPTY)
    }

    private fun redrawGridStatus(requestStatusType: RequestStatusView.RequestStatusType? = null) {
        if (requestStatusType == mRequestRequestStatusType) { // Already in the state
            return
        }

        val previousRequestStatus = mRequestRequestStatusType
        mRequestRequestStatusType = requestStatusType
        when {
            mRequestRequestStatusType == null -> {
                notifyItemRemoved(itemSize)
            }
            previousRequestStatus == null -> {
                notifyItemInserted(itemSize)
            }
            else -> {
                notifyItemChanged(itemSize)
            }
        }
    }

    override fun submitList(list: List<TModel>?) {
        redrawGridStatus()

        mItems = list ?: emptyList()

        super.submitList(list)
    }

    private val itemSize
        get() = super.getItemCount()

    @Deprecated("Use itemSize")
    override fun getItemCount(): Int {
        return super.getItemCount() + if (mRequestRequestStatusType == null) 0 else 1
    }

    protected abstract fun onCreateItemViewHolder(layoutInflater: LayoutInflater, parent: ViewGroup, viewType: Int): THolder

    protected abstract fun onBindItemViewHolder(holder: THolder, position: Int)
}

private enum class ViewType {
    GRID_STATUS,
    ITEM
}