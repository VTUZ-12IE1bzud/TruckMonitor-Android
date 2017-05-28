package ru.annin.truckmonitor.presentation.ui.activity

import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.arellomobile.mvp.presenter.ProvidePresenter
import ru.annin.truckmonitor.data.repository.SettingsRepository
import ru.annin.truckmonitor.presentation.presenter.SplashPresenter
import ru.annin.truckmonitor.presentation.ui.view.SplashView

/**
 * Экран "Заставка".
 *
 * @author Pavel Annin.
 */
class SplashActivity : MvpAppCompatActivity(), SplashView {

    // Component's
    @InjectPresenter(type = PresenterType.LOCAL)
    lateinit var presenter: SplashPresenter

    @ProvidePresenter(type = PresenterType.LOCAL)
    fun providePresenter() = SplashPresenter(SettingsRepository)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun navigate2Login() {
        LoginActivity.start(this)
        finish()
    }

    override fun navigate2Main() {
        MainActivity.start(this)
        finish()
    }
}