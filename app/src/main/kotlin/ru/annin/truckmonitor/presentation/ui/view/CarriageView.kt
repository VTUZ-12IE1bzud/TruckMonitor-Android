package ru.annin.truckmonitor.presentation.ui.view

import com.arellomobile.mvp.MvpView
import ru.annin.truckmonitor.presentation.ui.viewmodel.CarriageViewModel

/**
 * Интерфейс представления экрана "Грузоперевозки".
 *
 * @author Pavel Annin.
 */
interface CarriageView : MvpView {

    /** Отобразить данные. */
    fun showData(data: List<CarriageViewModel>?)
}