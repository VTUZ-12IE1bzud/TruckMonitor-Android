package ru.annin.truckmonitor.data.repository

import android.content.Context
import android.content.SharedPreferences
import ru.annin.truckmonitor.data.repository.SettingsRepository.isAuth

/**
 * Репозиторий инкапсулирующий в себя работу с настройками.
 *
 * @property isAuth Флаг указывающий на авторизированность пользователя.
 *
 * @author Pavel Annin.
 */
object SettingsRepository {

    private const val NAME = "Setting"
    private const val PREF_IS_AUTH = "IsAuth"

    // Component's
    private lateinit var prefs: SharedPreferences

    // Properties
    var isAuth: Boolean
        get() = prefs.getBoolean(PREF_IS_AUTH, false)
        set(value) = prefs.edit()
                .putBoolean(PREF_IS_AUTH, value)
                .apply()

    /**
     * Инициализация репозитория.
     * Выполнить в Application.
     */
    @JvmStatic fun initialization(context: Context) {
        prefs = context.applicationContext.getSharedPreferences(NAME, Context.MODE_PRIVATE)
    }

    /** Удалить настройку хранящую авторизированность пользователя. */
    fun clearIsAuth() = prefs.edit()
            .remove(PREF_IS_AUTH)
            .apply()
}