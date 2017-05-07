package ru.annin.truckmonitor.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.annin.truckmonitor.data.repository.SettingsRepository
import ru.annin.truckmonitor.presentation.ui.view.SplashView

/**
 * Presenter экрана "Заставка".
 *
 * @author Pavel Annin.
 */
@InjectViewState
class SplashPresenter(private val settingsRepository: SettingsRepository) : MvpPresenter<SplashView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        if (settingsRepository.isAuth) viewState.navigate2Main() else viewState.navigate2Login()
    }
}