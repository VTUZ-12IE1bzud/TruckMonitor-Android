package ru.annin.truckmonitor.presentation.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.annin.truckmonitor.R
import ru.annin.truckmonitor.presentation.ui.viewholder.ItemCarriageHeaderViewHolder
import ru.annin.truckmonitor.presentation.ui.viewholder.ItemCarriageViewHolder
import ru.annin.truckmonitor.presentation.ui.viewmodel.CarriageViewModel

/**
 * Адаптер грузоперевозок.
 *
 * @author Pavel Annin.
 */
class CarriageAdapter(var items: List<CarriageViewModel>? = null) :
        RecyclerView.Adapter<ItemCarriageViewHolder>(), StickyHeaderAdapter<ItemCarriageHeaderViewHolder>{

    override fun onCreateHeaderViewHolder(parent: ViewGroup): ItemCarriageHeaderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val vRoot: View = inflater.inflate(R.layout.item_carriages_header, parent, false)
        return ItemCarriageHeaderViewHolder(vRoot)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCarriageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val vRoot: View = inflater.inflate(R.layout.item_carriages, parent, false)
        return ItemCarriageViewHolder(vRoot)
    }

    override fun onBindHeaderViewHolder(viewHolder: ItemCarriageHeaderViewHolder, position: Int) {
        items?.get(position)?.let { (id) ->
            val start = items?.first { it.id == id }
            val end = items?.last { it.id == id }
            viewHolder.showDate(start?.planedDate, end?.planedDate)
        }
    }

    override fun onBindViewHolder(holder: ItemCarriageViewHolder, position: Int) {
        items?.get(position)?.let { holder.carriage = it }
    }

    override fun getHeaderId(position: Int) = items?.get(position)?.id ?: -1

    override fun getItemCount() = items?.size ?: 0
}