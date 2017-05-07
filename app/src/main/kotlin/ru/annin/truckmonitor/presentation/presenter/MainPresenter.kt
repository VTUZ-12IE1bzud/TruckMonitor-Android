package ru.annin.truckmonitor.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.annin.truckmonitor.data.repository.KeyStoreRepository
import ru.annin.truckmonitor.data.repository.SettingsRepository
import ru.annin.truckmonitor.presentation.ui.view.MainView
import ru.annin.truckmonitor.utils.Analytic
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.lang.kotlin.addTo
import rx.observables.SyncOnSubscribe
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

/**
 * Presenter главного экрана.
 *
 * @author Pavel Annin.
 */
@InjectViewState
class MainPresenter(private val keyStore: KeyStoreRepository,
                    private val settingsRepository: SettingsRepository) : MvpPresenter<MainView>() {

    // Component's
    private val rxSubscription = CompositeSubscription()

    override fun onDestroy() {
        super.onDestroy()
        rxSubscription.unsubscribe()
    }

    /** Выйти из аккаунта. */
    fun onLogOut() {
        Observable.create(SyncOnSubscribe.createStateless<Void> {
            keyStore.deleteToken()
            settingsRepository.clearIsAuth()

            it.onNext(null)
            it.onCompleted()
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { viewState.navigate2Login() },
                        { Analytic.error(it) }
                )
                .addTo(rxSubscription)
    }
}