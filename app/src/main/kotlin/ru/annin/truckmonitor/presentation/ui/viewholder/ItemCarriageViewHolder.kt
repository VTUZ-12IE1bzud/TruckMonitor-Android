package ru.annin.truckmonitor.presentation.ui.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import ru.annin.truckmonitor.R
import ru.annin.truckmonitor.presentation.ui.viewmodel.CarriageViewModel

/**
 * View Holder. Списка "Грузоперевозок".
 *
 * @property carriage Грузоперевозка.
 *
 * @author Pavel Annin.
 */
class ItemCarriageViewHolder(val vRoot: View) : RecyclerView.ViewHolder(vRoot) {

    // View's
    private val txtName: TextView by lazy { itemView.findViewById(R.id.txt_check_point_name) as TextView }
    private val txtAddress: TextView by lazy { itemView.findViewById(R.id.txt_check_point_address) as TextView }

    // Properties
    var carriage: CarriageViewModel? = null
        set(value) {
            txtName.text = value?.name
            txtAddress.text = value?.address
        }
}