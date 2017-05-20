
package ru.annin.truckmonitor.presentation.ui.adapter

import android.content.res.Resources
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import ru.annin.truckmonitor.R
import ru.annin.truckmonitor.presentation.ui.fragment.CarriageFragment
import ru.annin.truckmonitor.presentation.ui.viewmodel.CarriageViewModel

/**
 * @author Pavel Annin.
 */
class CarriagePagerAdapter(private val fm: FragmentManager,
                           private val resource: Resources,
                           var archiveCarriage: List<CarriageViewModel>? = null,
                           var onwardCarriage: List<CarriageViewModel>? = null) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int) = when(position) {
        0 -> CarriageFragment.newInstance(archiveCarriage?.let { ArrayList(it) } ?: ArrayList())
        1 -> CarriageFragment.newInstance(onwardCarriage?.let { ArrayList(it) } ?: ArrayList())
        else -> TODO()
    }

    override fun getPageTitle(position: Int) = when(position) {
        0 -> resource.getString(R.string.history_title_archive)
        1 -> resource.getString(R.string.history_title_onward)
        else -> TODO()
    }

    override fun getCount() = 2
}