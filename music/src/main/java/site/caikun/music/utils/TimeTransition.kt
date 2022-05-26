package site.caikun.music.utils

class TimeTransition {

    companion object {

        /**
         * 毫秒转换分秒
         * @param position 进度
         * @param duration 时长
         * @return 时间格式字符串 xx:xx/xx:xx
         */
        fun millisecondToMinute(position: Long, duration: Long): String {
            val p = "" + position / 1000 / 60 + ":" + position / 1000 % 60
            val d = "" + duration / 1000 / 60 + ":" + duration / 1000 % 60
            return "$p/$d"
        }
    }
}