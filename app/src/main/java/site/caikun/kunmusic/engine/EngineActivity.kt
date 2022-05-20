package site.caikun.kunmusic.engine

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class EngineActivity<T : ViewDataBinding>(private val layout: Int) : AppCompatActivity() {

    lateinit var binding: T
    lateinit var root: View

    override fun onStart() {
        super.onStart()
        supportActionBar?.hide()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        root = layoutInflater.inflate(layout, null)
        binding = DataBindingUtil.bind(root)!!
        setContentView(root)
        init()
    }

    abstract fun init()

    override fun onDestroy() {
        super.onDestroy()
    }
}