package ru.annin.truckmonitor.presentation.ui.viewmodel

import ru.annin.truckmonitor.domain.model.CarriageResponse
import java.util.*

/**
 * Модель представления "Грузопреревозки".
 *
 * @param id Идентификатор.
 * @param name Наименование.
 * @param address Адрес.
 * @param planedDate Планованя дата.
 *
 * @author Pavel Annin.
 */
data class CarriageViewModel(val id: Long, val name: String, val address: String, val planedDate: Date?) {

    companion object {
        @JvmStatic fun convert(data: List<CarriageResponse>): List<CarriageViewModel> {
            val result: MutableList<CarriageViewModel> = mutableListOf()
            data.forEach { (id, checkPoints) ->
                checkPoints.forEach { result.add(CarriageViewModel(id, it.name, it.address, it.plannedDate)) }
            }
            return result
        }
    }
}
