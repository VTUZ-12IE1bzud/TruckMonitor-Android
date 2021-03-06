package ru.annin.truckmonitor.presentation.ui.adapter

import android.content.res.Resources
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import ru.annin.truckmonitor.R
import ru.annin.truckmonitor.domain.model.CurrentCarriageResponse
import ru.annin.truckmonitor.presentation.ui.fragment.CheckPointFragment
import ru.annin.truckmonitor.presentation.ui.fragment.ContractFragment
import ru.annin.truckmonitor.presentation.ui.fragment.MapFragment

/**
 * @author Pavel Annin.
 */
class CurrentCarriagePagerAdapter(private val fm: FragmentManager,
                                  private val resource: Resources,
                                  var currentCarriage: CurrentCarriageResponse? = null) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int) = when (position) {
        0 -> ContractFragment.newInstance(currentCarriage?.carriages)
        1 -> CheckPointFragment.newInstance(currentCarriage?.checkPoints)
        2 -> MapFragment.newInstance(currentCarriage?.checkPoints)
        else -> TODO()
    }

    override fun getPageTitle(position: Int) = when (position) {
        0 -> resource.getString(R.string.main_tab_info)
        1 -> resource.getString(R.string.main_tab_check_point)
        2 -> resource.getString(R.string.main_tab_map)
        else -> TODO()
    }

    override fun getItemPosition(`object`: Any?): Int {
        if (`object` is ContractFragment) {
            `object`.updateContract(currentCarriage?.carriages)
        } else if (`object` is CheckPointFragment) {
            `object`.updateCheckPoints(currentCarriage?.checkPoints)
        } else if (`object` is MapFragment) {
            `object`.updateCheckPoints(currentCarriage?.checkPoints)
        }
        return super.getItemPosition(`object`)
    }

    override fun getCount() = 3
}