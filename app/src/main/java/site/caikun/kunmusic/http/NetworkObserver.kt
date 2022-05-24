package site.caikun.kunmusic.http

import site.caikun.kunmusic.http.error.ExceptionHandler
import site.caikun.kunmusic.http.error.ResponseException
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

abstract class NetworkObserver<T : Any> : Observer<T> {

    override fun onSubscribe(d: Disposable) {

    }

    override fun onNext(data: T) {
        onSuccess(data)
    }

    override fun onError(throwable: Throwable) {
        onFailure(ExceptionHandler.handleException(throwable))
    }

    override fun onComplete() {

    }

    protected abstract fun onSuccess(data: T)

    protected abstract fun onFailure(e: ResponseException)
}