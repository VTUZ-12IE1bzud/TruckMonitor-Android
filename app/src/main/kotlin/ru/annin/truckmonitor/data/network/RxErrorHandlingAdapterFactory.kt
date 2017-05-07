package ru.annin.truckmonitor.data.network

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.HttpException
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import ru.annin.truckmonitor.data.ApiException
import ru.annin.truckmonitor.data.NoInternetConnectionException
import ru.annin.truckmonitor.data.ServerNotAvailableException
import rx.Observable
import java.io.IOException
import java.lang.reflect.Type
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Фабрика поддержки RxJava. С обработкой исключений.
 *
 * @author Pavel Annin.
 */
class RxErrorHandlingAdapterFactory(
        private val original: RxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create()) : CallAdapter.Factory() {

    override fun get(returnType: Type?, annotations: Array<out Annotation>?, retrofit: Retrofit?): CallAdapter<*>
            = RxCallAdapterWrapper(original.get(returnType, annotations, retrofit))

    private class RxCallAdapterWrapper(val wrapped: CallAdapter<*>) : CallAdapter<Observable<*>> {

        override fun responseType(): Type = wrapped.responseType()

        override fun <R : Any?> adapt(call: Call<R>?): Observable<*>
                = (wrapped.adapt(call) as Observable<*>)
                .onErrorResumeNext { Observable.error(asRetrofitException(it)) }

        fun asRetrofitException(throwable: Throwable) = when (throwable) {
            is SocketTimeoutException -> ServerNotAvailableException()
            is UnknownHostException -> NoInternetConnectionException()
            is HttpException -> ApiException(code = throwable.response()?.code(), message = throwable.message)
            is IOException -> NoInternetConnectionException()
            else -> throwable
        }
    }
}