package ru.annin.truckmonitor.data.repository

import ru.annin.truckmonitor.data.ApiException
import ru.annin.truckmonitor.data.InvalidEmailPasswordException
import ru.annin.truckmonitor.data.network.TruckMonitorApiService
import ru.annin.truckmonitor.domain.model.LoginResponse
import rx.Observable

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
    fun login(email: String, password: String): Observable<LoginResponse>
            = service.login(email, password)
            .onErrorResumeNext {
                if (it is ApiException) {
                    // TODO: Баг сервера, должен возвращать 401
                    if (it.code == 400) {
                        return@onErrorResumeNext Observable.error(InvalidEmailPasswordException())
                    }
                }
                return@onErrorResumeNext Observable.error(it)
            }
}