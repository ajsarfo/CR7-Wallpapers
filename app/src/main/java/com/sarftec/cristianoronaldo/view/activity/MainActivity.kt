package com.sarftec.cristianoronaldo.view.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.sarftec.cristianoronaldo.R
import com.sarftec.cristianoronaldo.databinding.ActivityMainBinding
import com.sarftec.cristianoronaldo.view.advertisement.AdCountManager
import com.sarftec.cristianoronaldo.view.advertisement.InterstitialManager
import com.sarftec.cristianoronaldo.view.advertisement.RewardVideoManager
import com.sarftec.cristianoronaldo.view.dialog.LoadingDialog
import com.sarftec.cristianoronaldo.view.handler.NightModeHandler
import com.sarftec.cristianoronaldo.view.handler.ReadWriteHandler
import com.sarftec.cristianoronaldo.view.listener.CategoryFragmentListener
import com.sarftec.cristianoronaldo.view.listener.DrawerFragmentListener
import com.sarftec.cristianoronaldo.view.listener.QuoteFragmentListener
import com.sarftec.cristianoronaldo.view.listener.WallpaperFragmentListener
import com.sarftec.cristianoronaldo.view.manager.AppReviewManager
import com.sarftec.cristianoronaldo.view.model.CategoryUI
import com.sarftec.cristianoronaldo.view.model.WallpaperUI
import com.sarftec.cristianoronaldo.view.parcel.CategoryToDetail
import com.sarftec.cristianoronaldo.view.parcel.WallpaperToDetail
import com.sarftec.cristianoronaldo.view.utils.moreApps
import com.sarftec.cristianoronaldo.view.utils.rateApp
import com.sarftec.cristianoronaldo.view.utils.share
import com.sarftec.cristianoronaldo.view.viewmodel.MainViewModel
import com.sarftec.cristianoronaldo.view.viewmodel.WallpapersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity(), WallpaperFragmentListener, CategoryFragmentListener,
    DrawerFragmentListener, QuoteFragmentListener {

    private val layoutBinding by lazy {
        ActivityMainBinding.inflate(
            layoutInflater
        )
    }

    private val viewModel by viewModels<MainViewModel>()

    private lateinit var readWriteHandler: ReadWriteHandler

    private var drawerCallback: (() -> Unit)? = null

    private val nightModeHandler by lazy {
        NightModeHandler(this)
    }

    private val loadingDialog by lazy {
        LoadingDialog(this, layoutBinding.root)
    }

    private val rewardVideoManager by lazy {
        RewardVideoManager(
            this,
            R.string.quote_admob_reward_video_id,
            adRequestBuilder,
            networkManager
        )
    }

    private val appReviewManager by lazy {
        AppReviewManager(this)
    }

    override fun canShowInterstitial(): Boolean {
        return false
    }

    override fun createAdCounterManager(): AdCountManager {
        return AdCountManager(listOf(1, 3, 4, 2, 3))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        readWriteHandler = ReadWriteHandler(this)
        setStatusBarBackgroundLight()
        setContentView(layoutBinding.root)
        setupNavigationDrawer()
        setupNavigationView()
        setupNavigationHeader()
        setupNightMode()
        layoutBinding.bottomNavigation.setupWithNavController(getNavController())
    }

    private fun getNavController(): NavController {
        val navHost = supportFragmentManager.findFragmentById(
            R.id.nav_container
        ) as NavHostFragment
        return navHost.navController
    }

    private fun setupNavigationHeader() {
        layoutBinding.navigationView
            .getHeaderView(0)
            .findViewById<ImageView>(R.id.header_image)
            ?.let { imageView ->
                lifecycleScope.launchWhenCreated {
                    viewModel.getHeaderImage().let {
                        if (it.isSuccess()) {
                            Glide.with(this@MainActivity)
                                .load(it.data)
                                .into(imageView)
                        }
                        if (it.isError()) Log.v("TAG", "Header Image Error => ${it.message}")
                    }
                }
            }
    }


    private fun setupNightMode() {
        val switch =
            layoutBinding.navigationView.menu.findItem(R.id.night_mode).actionView as SwitchCompat
        switch.isChecked = (nightModeHandler.getMode() == NightModeHandler.Mode.NIGHT)
        switch.setOnCheckedChangeListener { _, isChecked ->
            nightModeHandler.changeMode(
                if (isChecked) NightModeHandler.Mode.NIGHT else NightModeHandler.Mode.DAY
            )
        }
    }

    private fun setDrawerCallback(callback: () -> Unit) {
        drawerCallback = callback
        layoutBinding.navigationDrawer.closeDrawer(GravityCompat.START)
    }

    private fun setupNavigationView() {
        layoutBinding.navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.upload -> {
                    setDrawerCallback {
                        navigateTo(UploadActivity::class.java)
                    }
                    true
                }
                R.id.share_app -> {
                    setDrawerCallback {
                        share(
                            "Hi take a look at this app ${getString(R.string.app_name)}. Its great! and you will like it too.",
                            "Share"
                        )
                    }
                    true
                }
                R.id.rate_app -> {
                    setDrawerCallback {
                        rateApp()
                    }
                    true
                }
                R.id.more_apps -> {
                    setDrawerCallback {
                        moreApps()
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun setupNavigationDrawer() {
        layoutBinding.navigationDrawer.addDrawerListener(
            object : DrawerLayout.DrawerListener {
                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

                override fun onDrawerOpened(drawerView: View) {
                }

                override fun onDrawerClosed(drawerView: View) {
                    drawerCallback?.invoke()
                    drawerCallback = null
                }

                override fun onDrawerStateChanged(newState: Int) {}
            }
        )
    }

    override fun openNavDrawer() {
        layoutBinding.navigationDrawer.openDrawer(GravityCompat.START)
    }

    override fun navigateOtherToDetail(
        wallpaperUI: WallpaperUI.Wallpaper,
        selection: WallpapersViewModel.Selection
    ) {
           navigateToWithParcel(
               DetailWallpaperActivity::class.java,
               parcel = WallpaperToDetail(
                   wallpaperUI.wallpaper.id,
                   getParcelSelection(selection)
               )
           )
    }

    override fun navigateToDetailCategory(categoryUI: CategoryUI.Category) {
        navigateToWithParcel(
            DetailCategoryActivity::class.java,
            parcel = CategoryToDetail(categoryUI.category.id)
        )
    }

    override fun onBackPressed() {
        if (layoutBinding.navigationDrawer.isDrawerOpen(GravityCompat.START)) {
            layoutBinding.navigationDrawer.closeDrawer(GravityCompat.START)
        } else super.onBackPressed()
    }

    private fun getParcelSelection(selection: WallpapersViewModel.Selection): Int {
        return when (selection) {
            WallpapersViewModel.Selection.POPULAR -> WallpaperToDetail.POPULAR
            WallpapersViewModel.Selection.RECENT -> WallpaperToDetail.RECENT
            WallpapersViewModel.Selection.FAVORITE -> WallpaperToDetail.FAVORITE
        }
    }

    override fun getReadWriteHandler(): ReadWriteHandler {
        return readWriteHandler
    }

    override fun showLoadingDialog(isShown: Boolean) {
        if(isShown) loadingDialog.show()
        else loadingDialog.dismiss()
    }

    override fun getRewardVideo(): RewardVideoManager {
        return rewardVideoManager
    }

    override fun getAdInterstitialManager(): InterstitialManager? {
        return interstitialManager
    }
}