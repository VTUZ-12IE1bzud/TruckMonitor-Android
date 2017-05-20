package ru.annin.truckmonitor.presentation.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

/**
 * @author Pavel Annin.
 */
interface StickyHeaderAdapter<VH : RecyclerView.ViewHolder> {

    fun onCreateHeaderViewHolder(parent: ViewGroup): VH
    fun onBindHeaderViewHolder(viewHolder: VH, position: Int)
    fun getHeaderId(position: Int): Long
}