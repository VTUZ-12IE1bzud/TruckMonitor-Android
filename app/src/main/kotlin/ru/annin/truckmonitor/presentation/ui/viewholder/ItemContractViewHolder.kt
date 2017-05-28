package ru.annin.truckmonitor.presentation.ui.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import ru.annin.truckmonitor.R
import ru.annin.truckmonitor.domain.model.ContractResponse

/**
 * View Holder списка "Контрактов".
 *
 * @property contract Договор.
 *
 * @author Pavel Annin.
 */
class ItemContractViewHolder(val vRoot: View) : RecyclerView.ViewHolder(vRoot) {

    // View's
    private val txtContractNumber by lazy { itemView.findViewById(R.id.txt_contract_number) as TextView }
    private val txtStores by lazy { itemView.findViewById(R.id.txt_stores) as TextView }

    // Listener's
    var onClick: (() -> Unit)? = null

    // Properties
    var contract: ContractResponse? = null
        set(value) {
            txtContractNumber.text = value?.number
            txtStores.text = value?.let { vRoot.resources.getString(R.string.contract_stores_format, it.before.name, it.from.name) }
        }

    init {
        vRoot.setOnClickListener { onClick?.invoke() }
    }
}