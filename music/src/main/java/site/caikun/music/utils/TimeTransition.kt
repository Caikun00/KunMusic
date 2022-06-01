package site.caikun.music.utils

class TimeTransition {

    companion object {

        /**
         * 转换分秒
         * @param time 毫秒时间 Long
         */
        private fun convert(time: Long): String {
            val minute = (time % 3600) / 60
            val second = (time % 3600) % 60

            //分字符串
            val minuteStr = if (minute == 0L) {
                "00"
            } else if (minute > 10) {
                "$minute" + ""
            } else {
                "0$minute"
            }

            //秒字符串
            val secondStr = if (second == 0L) {
                "00"
            } else if (second > 10) {
                "$second" + ""
            } else {
                "0$second"
            }
            return "$minuteStr:$secondStr"
        }

        /**
         * 毫秒转换分秒
         * @param position 进度
         * @param duration 时长
         * @return 时间格式字符串 xx:xx/xx:xx
         */
        fun millisecondToMinute(position: Long, duration: Long): String {
            return convert(position / 1000) + "/" + convert(duration / 1000)
        }
    }
}