package ru.annin.truckmonitor.presentation.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.annin.truckmonitor.R
import ru.annin.truckmonitor.domain.model.DetailCarriageResponse
import ru.annin.truckmonitor.presentation.ui.viewholder.ItemContractViewHolder

/**
 * Адаптер договоров.
 *
 * @author Pavel Annin.
 */
class ContractAdapter(var items: List<DetailCarriageResponse>? = null,
                      var onItemClick: ((DetailCarriageResponse) -> Unit)? = null) : RecyclerView.Adapter<ItemContractViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemContractViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val vRoot: View = inflater.inflate(R.layout.item_contract, parent, false)
        return ItemContractViewHolder(vRoot)
    }

    override fun onBindViewHolder(holder: ItemContractViewHolder, position: Int) {
        items?.get(position)?.let {
            holder.contract = it.contract
            holder.onClick = { onItemClick?.invoke(it) }
        }
    }

    override fun getItemCount() = items?.size ?: 0
}