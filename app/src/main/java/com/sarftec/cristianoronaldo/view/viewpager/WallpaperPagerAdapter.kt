package com.sarftec.cristianoronaldo.view.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sarftec.cristianoronaldo.R
import com.sarftec.cristianoronaldo.view.fragment_wallpaper.CategoryFragment
import com.sarftec.cristianoronaldo.view.fragment_wallpaper.FavoriteWallpaperFragment
import com.sarftec.cristianoronaldo.view.fragment_wallpaper.PopularWallpaperFragment
import com.sarftec.cristianoronaldo.view.fragment_wallpaper.RecentWallpaperFragment

class WallpaperPagerAdapter(private val activity: FragmentActivity)
    : FragmentStateAdapter(activity){

    override fun getItemCount(): Int {
        return activity.resources.getStringArray(R.array.wallpapers_sections).size
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> RecentWallpaperFragment()
            1 -> PopularWallpaperFragment()
            2 -> FavoriteWallpaperFragment()
            else -> CategoryFragment()
        }
    }
}