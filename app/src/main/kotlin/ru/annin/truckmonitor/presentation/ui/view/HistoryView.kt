package ru.annin.truckmonitor.presentation.ui.view

import com.arellomobile.mvp.MvpView
import ru.annin.truckmonitor.presentation.ui.viewmodel.CarriageViewModel

/**
 * Интерфейс представленя экрана "Грузоперевозки".
 *
 * @author Pavel Annin.
 */
interface HistoryView : MvpView {

    /** Закрыть экран. */
    fun navigate2Finish()

    /** Отобразить грузоперевозки. */
    fun showCarriage(archive: List<CarriageViewModel>?, onward: List<CarriageViewModel>?)

    /** Изменить состояние индикатора загрузки. */
    fun toggleLoading(isLoad: Boolean)

    /** Ошибка. */
    fun error(err: Throwable)
}