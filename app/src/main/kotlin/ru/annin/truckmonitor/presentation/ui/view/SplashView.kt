package ru.annin.truckmonitor.presentation.ui.view

import com.arellomobile.mvp.MvpView

/**
 * Интерфейс представления экрана "Заставка".
 *
 * @author Pavel Annin.
 */
interface SplashView : MvpView {

    /** Перейти на экран авторизации. */
    fun navigate2Login()

    /** Перейти на главный экран. */
    fun navigate2Main()
}