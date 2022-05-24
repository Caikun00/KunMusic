package site.caikun.music.utils

import androidx.lifecycle.LifecycleObserver
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

/**
 * 定时器管理
 */
class TimerTaskManager : LifecycleObserver {

    companion object {
        const val PROGRESS_UPDATE_INITIAL_INTERVAL = 500L
    }

    private var scheduledFuture: ScheduledFuture<*>? = null
    private var executor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
    private var updateProgressTask: Runnable? = null
    private var isRunning = false

    fun setUpdateProgressTask(runnable: Runnable) {
        updateProgressTask = runnable
    }

    /**
     * 开始进度更新
     */
    fun startUpdateProgress(time: Long = 1000) {
        stopUpdateProgress()
        if (!executor.isShutdown) {
            scheduledFuture = executor.scheduleAtFixedRate({
                updateProgressTask?.let {
                    isRunning = true
                    MainLooper.instance.ui(it)
                }
            }, PROGRESS_UPDATE_INITIAL_INTERVAL, time, TimeUnit.MILLISECONDS)
        }
    }

    fun stopUpdateProgress() {
        isRunning = false
        scheduledFuture?.cancel(false)
    }
}