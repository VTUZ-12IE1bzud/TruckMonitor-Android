package ru.annin.truckmonitor

import android.content.Context
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import ru.annin.truckmonitor.data.keystore.KeyStoreService
import ru.annin.truckmonitor.data.repository.KeyStoreRepository
import ru.annin.truckmonitor.data.repository.SettingsRepository

/**
 * Класс приложения.
 *
 * @author Pavel Annin.
 */
class TruckMonitorApplication : MultiDexApplication() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        // Setup
        KeyStoreRepository.initialization(KeyStoreService(this))
        SettingsRepository.initialization(this)
    }
}