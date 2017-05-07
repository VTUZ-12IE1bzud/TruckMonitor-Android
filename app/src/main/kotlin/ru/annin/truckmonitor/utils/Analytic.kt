package ru.annin.truckmonitor.utils

import android.util.Log
import ru.annin.truckmonitor.BuildConfig

/**
 * Объект инкапсулирующий в себя работу с аналитикой.
 *
 * @author Pavel Annin.
 */
object Analytic {

    // Parameter's
    private val TAG_LOG = "TruckMonitor"
    private val IS_DEBUG = BuildConfig.DEBUG

    /** Сообщить об ошибке. */
    fun error(err: Throwable) {
        if (IS_DEBUG) {
            Log.w(TAG_LOG, err)
        }
    }
}