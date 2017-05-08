package ru.annin.truckmonitor.presentation.ui.view

import com.arellomobile.mvp.MvpView
import ru.annin.truckmonitor.domain.model.UserInfoResponse

/**
 * Индикатор представления экрана "О водителе."
 *
 * @author Pavel Annin.
 */
interface MeView : MvpView {

    /** Закрыть экран. */
    fun navigate2Finish()

    /** Отобразить данные о пользователе. */
    fun showUserData(data: UserInfoResponse?)

    /** Сменить состояние индикатора загрузки. */
    fun toggleLoad(isLoad: Boolean)

    /** Отобпразить ошибку. */
    fun error(err: Throwable)
}