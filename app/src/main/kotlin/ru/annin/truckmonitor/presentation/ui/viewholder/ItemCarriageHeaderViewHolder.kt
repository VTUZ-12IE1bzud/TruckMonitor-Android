package ru.annin.truckmonitor.presentation.ui.viewholder

import android.support.v7.widget.RecyclerView
import android.text.format.DateFormat
import android.view.View
import android.widget.TextView
import ru.annin.truckmonitor.R
import ru.annin.truckmonitor.utils.safeLet
import ru.annin.truckmonitor.utils.visible
import java.util.*

/**
 * View Holder. Заголовка списка "Грузоперевозок".
 *
 * @author Pavel Annin.
 */
class ItemCarriageHeaderViewHolder(val vRoot: View) : RecyclerView.ViewHolder(vRoot) {

    // View's
    private val txtTitle: TextView by lazy { itemView.findViewById(R.id.txt_check_point_date) as TextView }

    fun showDate(start: Date?, end: Date?) = safeLet(start, end) { startDate, endDate ->
        val startFormat = DateFormat.getDateFormat(vRoot.context).format(startDate)
        val endFormat = DateFormat.getDateFormat(vRoot.context).format(endDate)
        txtTitle.text = vRoot.resources.getString(R.string.history_date_header_format, startFormat, endFormat)
        txtTitle.visible(true)
    } ?: txtTitle.visible(false)
}