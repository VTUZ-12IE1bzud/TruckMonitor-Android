package ru.annin.truckmonitor.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.annin.truckmonitor.data.repository.KeyStoreRepository
import ru.annin.truckmonitor.data.repository.RestApiRepository
import ru.annin.truckmonitor.presentation.ui.view.HistoryView
import ru.annin.truckmonitor.presentation.ui.viewmodel.CarriageViewModel
import ru.annin.truckmonitor.utils.Analytic
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.lang.kotlin.addTo
import rx.schedulers.Schedulers
import rx.subjects.BehaviorSubject
import rx.subscriptions.CompositeSubscription

/**
 * Presenter экрана "Грузоперевозки".
 *
 * @author Pavel Annin.
 */
@InjectViewState
class HistoryPresenter(private val apiRepository: RestApiRepository,
                       private val keyRepository: KeyStoreRepository) : MvpPresenter<HistoryView>() {

    private data class Carriages(val archive: List<CarriageViewModel>, val onward: List<CarriageViewModel>)

    // Component's
    private val rxSubscription = CompositeSubscription()
    private val rxLoading = BehaviorSubject.create(0)

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        operationLoading()

        loadCarriages { rxLoading.onNext(if (it) 1 else -1) }
    }

    override fun onDestroy() {
        super.onDestroy()
        rxSubscription.unsubscribe()
    }

    /** Вернутся на предыдущий экран. */
    fun onBack() = viewState.navigate2Finish()

    private fun operationLoading() {
        rxLoading.asObservable()
                .scan { sum: Int, item: Int -> sum + item }
                .map { it > 0 }
                .subscribe({ isLoading -> viewState.toggleLoading(isLoading) })
                .addTo(rxSubscription)
    }

    private fun loadCarriages(load: (Boolean) -> Unit) {
        load.invoke(true)
        Observable.just(keyRepository.token)
                .flatMap {
                    Observable.zip(
                            apiRepository.archiveCarriage(it),
                            apiRepository.onwardCarriage(it),
                            { archive, onward -> Carriages(CarriageViewModel.convert(archive), CarriageViewModel.convert(onward))}
                    )
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            viewState.showCarriage(it.archive, it.onward)
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