package com.artyomefimov.pocketdictionary.view.adapters

import android.support.v7.widget.RecyclerView
import android.view.View

abstract class AbstractAdapter<T>(
    var data: List<T>
) : RecyclerView.Adapter<AbstractViewHolder<T>>() {
    fun updateData(data: List<T>) {
        this.data = data
        notifyDataSetChanged()
    }
}

abstract class AbstractViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: T, position: Int, onLongClickAction: (String) -> Unit)
}