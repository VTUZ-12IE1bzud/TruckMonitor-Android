package ru.annin.truckmonitor.data.network

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import ru.annin.truckmonitor.BuildConfig
import java.util.concurrent.TimeUnit

/**
 * Сервис реализации REST API.
 * Объект конфигурирует:
 *  - Базовый URL;
 *  - Логи;
 *  - Тайминги.
 *
 *  @property service Сервис REST API.
 *
 * @author Pavel Annin.
 */
object TruckMonitorApiService {

    // Configuration's
    private const val SERVER_URL = BuildConfig.API_BASE_URL
    private val LOG_ENABLE = BuildConfig.DEBUG

    // Timings
    private const val TIMEOUT_SEC = 60L
    private const val TIMEOUT_READ_SEC = 60L
    private const val TIMEOUT_WRITE_SEC = 5 * 60L

    // Component's
    val service: TruckMonitorApi

    init {
        service = configRetrofit().create(TruckMonitorApi::class.java)
    }

    /** Конфигурация Retrofit. */
    private fun configRetrofit() = Retrofit.Builder().apply {
        baseUrl(SERVER_URL)
        addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        addConverterFactory(JacksonConverterFactory.create(ObjectMapper().registerKotlinModule()))
        client(configHttpClient())
    }.build()

    /** Конфигурация HttpClient. */
    private fun configHttpClient() = OkHttpClient.Builder().apply {
        connectTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
        readTimeout(TIMEOUT_READ_SEC, TimeUnit.SECONDS)
        writeTimeout(TIMEOUT_WRITE_SEC, TimeUnit.SECONDS)
        addInterceptor(configLoggingInterceptor())
    }.build()

    /** Конфигурация логов. */
    private fun configLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = if (LOG_ENABLE) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }
}