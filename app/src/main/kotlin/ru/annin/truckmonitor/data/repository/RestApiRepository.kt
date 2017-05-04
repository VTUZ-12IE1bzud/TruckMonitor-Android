package ru.annin.truckmonitor.data.repository

import ru.annin.truckmonitor.data.network.TruckMonitorApiService

/**
 * Репозиторий инкапсулирующий в себя работу с сервером.
 *
 * @author Pavel Annin.
 */
object RestApiRepository {

    // Component's
    private val service by lazy { TruckMonitorApiService.service }

    /**
     * Авторизация.
     *
     * @param email Email пользователя.
     * @param password Пароль пользователя.
     */
    fun login(email: String, password: String) = service.login(email, password)
}