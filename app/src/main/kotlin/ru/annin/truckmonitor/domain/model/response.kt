package ru.annin.truckmonitor.domain.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

/**
 * Файл инкапсулирующий в себя модели ответов сервера.
 *
 * @author Pavel Annin.
 */

/**
 * Модель данных "Авторизация".
 *
 * @param token Токен пользователя.
 */
data class LoginResponse(@JsonProperty(value = "token", required = true) val token: String): Serializable