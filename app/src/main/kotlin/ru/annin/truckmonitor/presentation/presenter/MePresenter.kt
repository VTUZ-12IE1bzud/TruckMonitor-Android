package ru.annin.truckmonitor.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.annin.truckmonitor.data.repository.KeyStoreRepository
import ru.annin.truckmonitor.data.repository.RestApiRepository
import ru.annin.truckmonitor.presentation.ui.view.MeView
import ru.annin.truckmonitor.utils.Analytic
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.lang.kotlin.addTo
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

/**
 * Presenter экрана "О водителе".
 *
 * @author Pavel Annin.
 */
@InjectViewState
class MePresenter(private val apiRepository: RestApiRepository,
                  private val keyRepository: KeyStoreRepository) : MvpPresenter<MeView>() {

    // Component's
    private val rxSubscription = CompositeSubscription()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadUserInfo { viewState.toggleLoad(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        rxSubscription.unsubscribe()
    }

    /** Перейти назад. */
    fun onBack() = viewState.navigate2Finish()

    /** Загрузить информацию о пользователе. */
    private fun loadUserInfo(load: (Boolean) -> Unit) {
        load.invoke(true)
        Observable.just(keyRepository.token)
                .flatMap { apiRepository.userInfo(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            viewState.showUserData(it)
                            load.invoke(false)
                        },
                        {
                            load.invoke(false)
                            it?.let { viewState.error(it) }
                            Analytic.error(it)
                        }
                )
                .addTo(rxSubscription)
    }
}