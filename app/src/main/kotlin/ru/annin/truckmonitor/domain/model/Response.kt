package ru.annin.truckmonitor.domain.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.util.ISO8601Utils
import ru.annin.truckmonitor.utils.Analytic
import java.io.Serializable
import java.text.ParsePosition
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
data class LoginResponse(@JsonProperty(value = "token", required = true) val token: String) : Serializable

/**
 * Модель данных "Текущая грузоперевозка".
 *
 * @param id Идентификатор.
 * @param carriages Детализация грузоперевозки.
 * @param checkPoints Контрольнаы точки.
 */
data class CurrentCarriageResponse(@JsonProperty(value = "id", required = true) val id: Long,
                                   @JsonProperty(value = "carriages", required = true) val carriages: List<DetailCarriageResponse>,
                                   @JsonProperty(value = "checkPoints", required = true) val checkPoints: List<CheckPoints>) : Serializable

/**
 * Модель данных "Грузоперевозок".
 *
 * @param id Идентификатор.
 * @param checkPoints Контрольнаы точки.
 */
data class CarriageResponse(@JsonProperty(value = "id", required = true) val id: Long,
                            @JsonProperty(value = "checkPoints", required = true) val checkPoints: List<CheckPoints>) : Serializable

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
                            @JsonProperty(value = "phone", required = true) val phone: String) : Serializable {

    // Properties
    val birthDate: Date?
        get() = try {
            ISO8601Utils.parse(sBirthDate, ParsePosition(0))
        } catch (err: Exception) {
            Analytic.error(err)
            null
        }
}

/**
 * Модель данных "Контрольная точка".
 *
 * @param id Идентификатор.
 * @param name Наименование.
 * @param address Адрес.
 * @param coordinate Координаты.
 *
 * @property plannedDate Плановая дата.
 * @property factDate Фактическая дата.
 */
data class CheckPoints(@JsonProperty(value = "id", required = true) val id: Long,
                       @JsonProperty(value = "name", required = true) val name: String,
                       @JsonProperty(value = "address", required = true) val address: String,
                       @JsonProperty(value = "coordinates", required = true) val coordinate: CoordinateResponse,
                       @JsonProperty(value = "planned", required = true) val planned: String,
                       @JsonProperty(value = "fact", required = false) val fact: String? = null) : Serializable {

    // Properties
    val plannedDate: Date?
        get() = try {
            ISO8601Utils.parse(planned, ParsePosition(0))
        } catch (err: Exception) {
            Analytic.error(err)
            null
        }
    // Properties
    val factDate: Date?
        get() = try {
            ISO8601Utils.parse(fact, ParsePosition(0))
        } catch (err: Exception) {
            Analytic.error(err)
            null
        }
}

/**
 * Модель данных "Координаты".
 *
 * @param latitude Широта.
 * @param longitude Долгота.
 */
data class CoordinateResponse(@JsonProperty(value = "latitude", required = true) val latitude: Float,
                              @JsonProperty(value = "longitude", required = true) val longitude: Float) : Serializable

/**
 * Модель данных "Детализация грузоперевозки.".
 *
 * @param contract Контракт.
 * @param nomenclatures Номерклатура.
 */
data class DetailCarriageResponse(@JsonProperty(value = "contract", required = true) val contract: ContractResponse,
                                  @JsonProperty(value = "nomenclatures", required = true) val nomenclatures: List<NomenclatureResponse>) : Serializable

/**
 * Модель данных "Контракт".
 *
 * @param number Номер.
 * @param customsLink Ссылка на таможенную дикларацию.
 * @param from Склад загрузки.
 * @param before Склад разгрузки.
 * @param packaging Упаковка.
 * @param manager Менеджер.
 */
data class ContractResponse(@JsonProperty(value = "number", required = true) val number: String,
                            @JsonProperty(value = "customsLink", required = true) val customsLink: String,
                            @JsonProperty(value = "from", required = true) val from: StoreResponse,
                            @JsonProperty(value = "before", required = true) val before: StoreResponse,
                            @JsonProperty(value = "packaging", required = true) val packaging: String,
                            @JsonProperty(value = "manager", required = true) val manager: ManagerResponse) : Serializable

/**
 * Модель данных "Склад".
 *
 * @param name Наименвоание.
 * @param address Адрес.
 */
data class StoreResponse(@JsonProperty(value = "name", required = true) val name: String,
                         @JsonProperty(value = "address", required = true) val address: String) : Serializable


/**
 * Модель данных "Номенклатуры".
 *
 * @param name Наименвоание.
 * @param amount Количество.
 * @param measure Единица измерения.
 */
data class NomenclatureResponse(@JsonProperty(value = "name", required = true) val name: String,
                                @JsonProperty(value = "amount", required = true) val amount: Float,
                                @JsonProperty(value = "measure", required = true) val measure: String) : Serializable

/**
 * Модель данных "Менеджер".
 *
 * @param name Имя менеджера.
 * @param photolink Фотография менеджера.
 * @param phone Номер телефона.
 */
data class ManagerResponse(@JsonProperty(value = "name", required = true) val name: String,
                           @JsonProperty(value = "photolink", required = true) val photolink: String,
                           @JsonProperty(value = "phone", required = true) val phone: String) : Serializable
