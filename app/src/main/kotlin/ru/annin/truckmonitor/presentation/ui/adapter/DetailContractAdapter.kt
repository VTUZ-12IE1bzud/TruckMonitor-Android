package ru.annin.truckmonitor.presentation.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.annin.truckmonitor.R
import ru.annin.truckmonitor.domain.model.NomenclatureResponse
import ru.annin.truckmonitor.presentation.ui.viewholder.ItemDetailContractViewHolder
import ru.annin.truckmonitor.utils.safeLet

/**
 * Адаптер содержимого договора.
 *
 * @author Pavel Annin.
 */
class DetailContractAdapter(var items: List<NomenclatureResponse>? = null,
                            var packaging: String? = null) : RecyclerView.Adapter<ItemDetailContractViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemDetailContractViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val vRoot: View = inflater.inflate(R.layout.item_detail_contract, parent, false)
        return ItemDetailContractViewHolder(vRoot)
    }

    override fun onBindViewHolder(holder: ItemDetailContractViewHolder, position: Int) {
        safeLet(items?.get(position), packaging) { nomeclature, packaging ->
            holder.show(nomeclature, packaging)
        }
    }

    override fun getItemCount() = items?.size ?: 0
}