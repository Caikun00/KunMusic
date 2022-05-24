package site.caikun.kunmusic.http

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * Rxjava线程切换
 */
class SchedulerProvider {

    companion object {

        @JvmStatic
        fun io(): Scheduler {
            return Schedulers.io()
        }

        @JvmStatic
        fun ui(): Scheduler {
            return AndroidSchedulers.mainThread()
        }

        @JvmStatic
        fun <T : Any> applySchedulers(): ObservableTransformer<T, T> {
            return ObservableTransformer {
                it.subscribeOn(io()).observeOn(ui())
            }
        }
    }
}