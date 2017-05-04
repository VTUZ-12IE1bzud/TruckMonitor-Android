package ru.annin.truckmonitor

import android.content.Context
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication

/**
 * Класс приложения.
 *
 * @author Pavel Annin.
 */
class TruckMonitorApplication : MultiDexApplication() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }
}