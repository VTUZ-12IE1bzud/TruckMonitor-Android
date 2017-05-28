package ru.annin.truckmonitor.presentation.ui.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import ru.annin.truckmonitor.R
import ru.annin.truckmonitor.domain.model.NomenclatureResponse

/**
 * View Holder детализированного списка "Контракта".
 *
 * @author Pavel Annin.
 */
class ItemDetailContractViewHolder(val vRoot: View) : RecyclerView.ViewHolder(vRoot) {

    // View's
    private val txtNomenclatureName by lazy { itemView.findViewById(R.id.txt_nomenclature_name) as TextView }
    private val txtNomenclatureAmount by lazy { itemView.findViewById(R.id.txt_amount) as TextView }
    private val txtNomenclaturePackaging by lazy { itemView.findViewById(R.id.txt_packaging) as TextView }

    fun show(nomenclature: NomenclatureResponse, packaging: String) {
        nomenclature.run {
            txtNomenclatureName.text = name
            txtNomenclatureAmount.text = vRoot.resources.getString(R.string.detail_contract_nomenclature_amount, amount.toString(), measure)
        }
        txtNomenclaturePackaging.text = vRoot.resources.getString(R.string.detail_contract_nomenclature_packaging, packaging)
    }
}