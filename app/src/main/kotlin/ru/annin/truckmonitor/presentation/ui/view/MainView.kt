package ru.annin.truckmonitor.presentation.ui.view

import com.arellomobile.mvp.MvpView

/**
 * Интерфейс представления главного экрана.
 *
 * @author Pavel Annin.
 */
interface MainView : MvpView {

    /** Перейти на экран "Авторизации". */
    fun navigate2Login()

    /** Перейти на экран "Информация о водителе". */
    fun navigate2Me()

    /** Перейти на экран "Информация о грузоперевозках". */
    fun navigate2History()

    /** Изменить состояние индикатор загрузки. */
    fun toggleLoad(isLoad: Boolean)

    /** Оттбразить сообщение об ошибке. */
    fun error(err: Throwable)
}