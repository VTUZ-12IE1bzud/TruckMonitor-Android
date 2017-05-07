package ru.annin.truckmonitor.presentation.presenter

import android.util.Patterns
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.annin.truckmonitor.data.repository.KeyStoreRepository
import ru.annin.truckmonitor.data.repository.RestApiRepository
import ru.annin.truckmonitor.data.repository.SettingsRepository
import ru.annin.truckmonitor.presentation.ui.view.LoginView
import ru.annin.truckmonitor.utils.Analytic
import rx.android.schedulers.AndroidSchedulers
import rx.lang.kotlin.addTo
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

/**
 * Presenter экрана "Авторизации".
 *
 * @author Pavel Annin.
 */
@InjectViewState
class LoginPresenter(private val apiRepository: RestApiRepository,
                     private val keyRepository: KeyStoreRepository,
                     private val settingsRepository: SettingsRepository) : MvpPresenter<LoginView>() {

    // Component's
    private val rxSubscription = CompositeSubscription()

    override fun onDestroy() {
        super.onDestroy()
        rxSubscription.unsubscribe()
    }

    /** Валидация пользовательских данных. */
    fun onValid(email: CharSequence, password: CharSequence)
            = viewState.toggleValid(valid(email, password))

    /** Авторизоваться. */
    fun onSignIn(email: CharSequence, password: CharSequence)
            = requestSignIn(email, password) { viewState.toggleLoad(it) }

    private fun valid(email: CharSequence?, password: CharSequence?): Boolean {
        var isValid = true

        if (email.isNullOrBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            isValid = false
        }
        if (password.isNullOrBlank()) {
            isValid = false
        }
        return isValid
    }

    private fun requestSignIn(email: CharSequence, password: CharSequence, load: (Boolean) -> Unit) {
        load.invoke(true)
        apiRepository.login(email.toString(), password.toString())
                .doOnNext { keyRepository.token = it.token }
                .doOnNext { settingsRepository.isAuth = true }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            load.invoke(false)
                            viewState.navigate2Main()
                        },
                        {
                            it?.let { viewState.error(it) }
                            load.invoke(false)
                            Analytic.error(it)
                        }
                ).addTo(rxSubscription)
    }
}