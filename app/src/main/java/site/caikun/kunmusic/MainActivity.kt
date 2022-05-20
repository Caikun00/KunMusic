package site.caikun.kunmusic

import android.graphics.Color
import androidx.fragment.app.Fragment
import com.drake.statusbar.immersive
import com.google.android.material.tabs.TabLayoutMediator
import site.caikun.kunmusic.adapter.ViewPagerAdapter
import site.caikun.kunmusic.databinding.ActivityMainBinding
import site.caikun.kunmusic.engine.EngineActivity
import site.caikun.kunmusic.fragment.MusicApiFragment
import site.caikun.kunmusic.fragment.MusicStateFragment

class MainActivity : EngineActivity<ActivityMainBinding>(R.layout.activity_main) {

    override fun onStart() {
        super.onStart()
        immersive(Color.WHITE, true)
    }

    override fun init() {
        val fragmentList = mutableListOf<Fragment>()
        val fragmentTitle = mutableListOf<String>("state", "api")
        fragmentList.add(MusicStateFragment())
        fragmentList.add(MusicApiFragment())

        val adapter = ViewPagerAdapter(this, fragmentList)
        binding.viewPager.adapter = adapter

        val tabLayoutMediator = TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager,
            true
        ) { tab, position ->
            tab.text = fragmentTitle[position]
        }
        tabLayoutMediator.attach()
    }
}