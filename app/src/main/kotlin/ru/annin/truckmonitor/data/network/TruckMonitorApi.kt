package ru.annin.truckmonitor.data.network

import retrofit2.http.GET
import retrofit2.http.Query
import ru.annin.truckmonitor.domain.model.LoginResponse
import rx.Observable

/**
 * Rest API.
 *
 * @author Pavel Annin.
 */
interface TruckMonitorApi {

    /**
     * Авторизация.
     *
     * @param email Email пользователя.
     * @param password Пароль пользователя.
     */
    @GET("/api/v1/login")
    fun login(@Query("email") email: String,
              @Query("password") password: String): Observable<LoginResponse>
}