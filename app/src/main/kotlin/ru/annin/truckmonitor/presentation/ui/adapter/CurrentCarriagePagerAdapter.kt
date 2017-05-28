package ru.annin.truckmonitor.presentation.ui.adapter

import android.content.res.Resources
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import ru.annin.truckmonitor.R
import ru.annin.truckmonitor.domain.model.CurrentCarriageResponse
import ru.annin.truckmonitor.presentation.ui.fragment.ContractFragment

/**
 * @author Pavel Annin.
 */
class CurrentCarriagePagerAdapter(private val fm: FragmentManager,
                                  private val resource: Resources,
                                  var currentCarriage: CurrentCarriageResponse? = null) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int) = when (position) {
        0 -> ContractFragment.newInstance(currentCarriage?.carriages)
        else -> TODO()
    }

    override fun getPageTitle(position: Int) = when (position) {
        0 -> resource.getString(R.string.history_title_archive)
        else -> TODO()
    }

    override fun getItemPosition(`object`: Any?): Int {
        if (`object` is ContractFragment) {
            `object`.updateContract(currentCarriage?.carriages)
        }
        return super.getItemPosition(`object`)
    }

    override fun getCount() = 1
}