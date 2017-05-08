package ru.annin.truckmonitor.domain.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

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

/**
 * Модель данных "Информация о водителе".
 *
 * @param role Роль пользователя.
 * @param surname Фамилия пользователя.
 * @param name Имя пользователя.
 * @param patronymic Отчество пользователя.
 * @param email Email пользователя.
 * @param photo Фото пользователя.
 * @param phone Телефон пользователя.

 * @property birthDate Дата рождения пользователя.
 */
data class UserInfoResponse(@JsonProperty(value = "role", required = true) val role: String,
                            @JsonProperty(value = "surname", required = true) val surname: String,
                            @JsonProperty(value = "name", required = true) val name: String,
                            @JsonProperty(value = "patronymic", required = false) val patronymic: String?,
                            @JsonProperty(value = "birthDate", required = true) val sBirthDate: String,
                            @JsonProperty(value = "email", required = true) val email: String,
                            @JsonProperty(value = "photo", required = true) val photo: String,
                            @JsonProperty(value = "phone", required = true) val phone: String): Serializable {

    // Properties
    val birthDate: Date
        get() = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'").parse(sBirthDate)
}