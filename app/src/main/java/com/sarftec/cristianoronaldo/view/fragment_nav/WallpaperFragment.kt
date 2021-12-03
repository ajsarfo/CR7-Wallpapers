package com.sarftec.cristianoronaldo.view.fragment_nav

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.sarftec.cristianoronaldo.R
import com.sarftec.cristianoronaldo.databinding.FragmentWallpapersBinding
import com.sarftec.cristianoronaldo.view.listener.DrawerFragmentListener
import com.sarftec.cristianoronaldo.view.viewpager.WallpaperPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WallpaperFragment : Fragment() {

    private lateinit var layoutBinding: FragmentWallpapersBinding

    private var drawerFragmentListener: DrawerFragmentListener? = null

    override fun onAttach(context: Context) {
        if(context is DrawerFragmentListener) drawerFragmentListener = context
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        layoutBinding = FragmentWallpapersBinding.inflate(
            layoutInflater,
            container,
            false
        )
        setupViewPager()
        setupTabLayout()
        layoutBinding.toolbar.setNavigationOnClickListener {
            drawerFragmentListener?.openNavDrawer()
        }
        return layoutBinding.root
    }

    private fun setupTabLayout() {
        val tabHeadings = resources.getStringArray(R.array.wallpapers_sections)
        TabLayoutMediator(
            layoutBinding.tabLayout,
            layoutBinding.viewPager
        ) { tab, position ->
            tab.text = tabHeadings[position]
        }.attach()
    }

    private fun setupViewPager() {
        layoutBinding.viewPager.adapter = WallpaperPagerAdapter(requireActivity())
    }
}