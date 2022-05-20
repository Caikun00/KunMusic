package site.caikun.kunmusic.engine

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import java.lang.Exception

abstract class EngineFragment<VDB : ViewDataBinding>(@LayoutRes var layout: Int = 0) : Fragment() {

    lateinit var binding: VDB

    var TAG : String = this.javaClass.name

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layout, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            init()
        } catch (e: Exception) {
            Log.e("Engine", "Initialization failed")
            e.printStackTrace()
        }
    }

    protected abstract fun init()

    protected fun navigation(): NavController {
        return NavHostFragment.findNavController(this)
    }

    protected fun <T : ViewModel> fragmentScopeViewModel(model: Class<T>): T {
        return ViewModelProvider(this).get(model)
    }

    protected fun <T : ViewModel> activityScopeViewModel(model: Class<T>): T {
        return ViewModelProvider(requireActivity()).get(model)
    }

    protected fun <T : ViewModel> applicationScopeViewModel(model: Class<T>): T {
        return ViewModelProvider(context?.applicationContext as EngineApplication).get(model)
    }
}