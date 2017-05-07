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
}