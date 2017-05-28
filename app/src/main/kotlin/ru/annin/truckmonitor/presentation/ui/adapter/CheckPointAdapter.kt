package ru.annin.truckmonitor.presentation.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.annin.truckmonitor.R
import ru.annin.truckmonitor.domain.model.CheckPoints
import ru.annin.truckmonitor.presentation.ui.viewholder.ItemCheckPointViewHolder

/**
 * Адаптер контрольных точек.
 *
 * @author Pavel Annin.
 */
class CheckPointAdapter(var items: List<CheckPoints>? = null) : RecyclerView.Adapter<ItemCheckPointViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCheckPointViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val vRoot: View = inflater.inflate(R.layout.item_check_point, parent, false)
        return ItemCheckPointViewHolder(vRoot)
    }

    override fun onBindViewHolder(holder: ItemCheckPointViewHolder, position: Int) {
        items?.get(position)?.let {
            holder.checkPoint = it
        }
    }

    override fun getItemCount() = items?.size ?: 0
}