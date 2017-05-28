package ru.annin.truckmonitor.presentation.ui.viewholder

import android.support.v7.widget.RecyclerView
import android.text.format.DateFormat
import android.view.View
import android.widget.TextView
import ru.annin.truckmonitor.R
import ru.annin.truckmonitor.domain.model.CheckPoints
import ru.annin.truckmonitor.utils.visible

/**
 * View Holder. Списка "Контрольных точек".
 *
 * @property checkPoint Контрольная точка.
 *
 * @author Pavel Annin.
 */
class ItemCheckPointViewHolder(val vRoot: View) : RecyclerView.ViewHolder(vRoot) {

    // View's
    private val txtName: TextView by lazy { itemView.findViewById(R.id.txt_check_point_name) as TextView }
    private val txtAddress: TextView by lazy { itemView.findViewById(R.id.txt_check_point_address) as TextView }
    private val txtPlanned: TextView by lazy { itemView.findViewById(R.id.txt_check_point_planed) as TextView }
    private val txtFact: TextView by lazy { itemView.findViewById(R.id.txt_check_point_fact) as TextView }

    // Properties
    var checkPoint: CheckPoints? = null
        set(value) {
            txtName.text = value?.name
            txtAddress.text = value?.address
            txtPlanned.text = value?.plannedDate?.let { vRoot.resources.getString(R.string.check_point_planned, "${DateFormat.getLongDateFormat(vRoot.context).format(it)} ${DateFormat.getTimeFormat(vRoot.context).format(it)}") }
            txtFact.text = value?.factDate?.let { vRoot.resources.getString(R.string.check_point_fact, "${DateFormat.getLongDateFormat(vRoot.context).format(it)} ${DateFormat.getTimeFormat(vRoot.context).format(it)}").format(it) }
            txtFact.visible(value?.factDate != null)
        }
}