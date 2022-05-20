package site.caikun.kunmusic.engine

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.size.ViewSizeResolver

class CommonBindAdapter {

    companion object{

        @BindingAdapter("imageLoader")
        @JvmStatic
        fun imageLoader(imageView: ImageView, url: String?) {
            if (!url.isNullOrEmpty()) {
                imageView.load(url) {
                    crossfade(true)
                    size(ViewSizeResolver(imageView))
                }
            }
        }
    }
}