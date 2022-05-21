package site.caikun.kunmusic.utils

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.widget.Toast

class ToastUtil {

    companion object {
        @SuppressLint("StaticFieldLeak")
        var context: Context? = null

        var toast: Toast? = null

        fun init(context: Context){
            this.context = context
        }

        fun show(message:String){
            if(context != null && TextUtils.isEmpty(message)){
                toast?.cancel()
            }

            toast = Toast.makeText(context,"",Toast.LENGTH_LONG)
            toast?.setText(message)
            toast?.show()
        }
    }
}