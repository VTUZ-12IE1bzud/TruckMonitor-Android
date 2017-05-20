package ru.annin.truckmonitor.data.network

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import ru.annin.truckmonitor.domain.model.CarriageResponse
import ru.annin.truckmonitor.domain.model.LoginResponse
import ru.annin.truckmonitor.domain.model.UserInfoResponse
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

    /**
     * Информация о архивных грузоперевозках.
     *
     * @param token Токен пользователя.
     */
    @GET("/api/v1/carriage/archive")
    fun archiveCarriage(@Header("X-AUTH-TOKEN") token: String): Observable<List<CarriageResponse>>

    /**
     * Информация о будущих грузоперевозках.
     *
     * @param token Токен пользователя.
     */
    @GET("/api/v1/carriage/onward")
    fun onwardCarriage(@Header("X-AUTH-TOKEN") token: String): Observable<List<CarriageResponse>>

    /**
     * Информация о водителе.
     *
     * @param token Токен пользователя.
     */
    @GET("/api/v1/me")
    fun userInfo(@Header("X-AUTH-TOKEN") token: String): Observable<UserInfoResponse>
}