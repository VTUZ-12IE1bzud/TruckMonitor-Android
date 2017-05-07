package ru.annin.truckmonitor.presentation.ui.view

import com.arellomobile.mvp.MvpView

/**
 * Интерфейс представления экрана "Авторизация".
 *
 * @author Pavel Annin.
 */
interface LoginView : MvpView {

    /** Перейти на главный экран. */
    fun navigate2Main()

    /** Изменить состояние валидности данных. */
    fun toggleValid(isValid: Boolean)

    /** Изменить состояние индикатор загрузки. */
    fun toggleLoad(isLoad: Boolean)

    fun error(err: Throwable)
}