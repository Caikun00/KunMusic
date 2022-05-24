package site.caikun.music.listener

interface OnPlayProgressListener {

    /**
     * @param position 进度
     * @param duration 时长
     * @param buffered 缓冲
     */
    fun onPlayProgress(position: Long, duration: Long, buffered: Long)
}