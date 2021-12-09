package com.sarftec.cristianoronaldo.view.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.viewpager2.widget.ViewPager2
import com.sarftec.cristianoronaldo.R
import com.sarftec.cristianoronaldo.databinding.ActivityDetailBinding
import com.sarftec.cristianoronaldo.view.adapter.WallpaperDetailAdapter
import com.sarftec.cristianoronaldo.view.advertisement.AdCountManager
import com.sarftec.cristianoronaldo.view.advertisement.BannerManager
import com.sarftec.cristianoronaldo.view.advertisement.RewardVideoManager
import com.sarftec.cristianoronaldo.view.dialog.LoadingDialog
import com.sarftec.cristianoronaldo.view.dialog.WallpaperSetDialog
import com.sarftec.cristianoronaldo.view.handler.ReadWriteHandler
import com.sarftec.cristianoronaldo.view.handler.ToolingHandler
import com.sarftec.cristianoronaldo.view.utils.downloadGlideImage
import com.sarftec.cristianoronaldo.view.utils.toast
import com.sarftec.cristianoronaldo.view.viewmodel.DetailBaseViewModel
import com.sarftec.cristianoronaldo.view.viewpager.ZoomOutPageTransformer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

abstract class DetailBaseActivity<T : Parcelable> : BaseActivity() {

    private val layoutBinding by lazy {
        ActivityDetailBinding.inflate(
            layoutInflater
        )
    }

    protected abstract val viewModel: DetailBaseViewModel<T>

    private val detailAdapter by lazy {
        WallpaperDetailAdapter(lifecycleScope, viewModel)
    }

    private lateinit var readWriteHandler: ReadWriteHandler

    private val toolingHandler by lazy {
        ToolingHandler(this, readWriteHandler)
    }

    private val loadingDialog by lazy {
        LoadingDialog(this, layoutBinding.root)
    }

    private val wallpaperDialog by lazy {
        WallpaperSetDialog(
            layoutBinding.root,
            onHome = {
                runCurrentBitmapCallback {
                    toolingHandler.setWallpaper(it, ToolingHandler.WallpaperOption.HOME)
                }
            },
            onLock = {
                runCurrentBitmapCallback {
                    toolingHandler.setWallpaper(it, ToolingHandler.WallpaperOption.LOCK)
                }
            }
        )
    }

    private val rewardVideoManager by lazy {
        RewardVideoManager(
            this,
            rewardId(),
            adRequestBuilder,
            networkManager
        )
    }

    override fun createAdCounterManager(): AdCountManager {
        return AdCountManager(listOf(3, 5, 8, 12))
    }

    protected abstract fun bannerId(): Int

    protected abstract fun rewardId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*************** Admob Configuration ********************/
        BannerManager(this, adRequestBuilder).attachBannerAd(
            getString(bannerId()),
            layoutBinding.mainBanner
        )
        /**********************************************************/
        setStatusBarBackgroundLight()
        setContentView(layoutBinding.root)
        readWriteHandler = ReadWriteHandler(this)
        setupClickListeners()
        setupViewPager()
        setupLoadingIndicator()
        getParcelFromIntent<T>(intent)?.let {
            viewModel.setParcel(it)
        }
        viewModel.loadWallpaperFlow()
        observeFlow()
    }

    private fun observeFlow() {
        viewModel.wallpaperFlow.observe(this) { resources ->
            if (resources.isLoading()) showLayout(false)
            if (resources.isError()) Log.v("TAG", "${resources.message}")
            if (resources.isSuccess()) resources.data?.let { flow ->
                lifecycleScope.launchWhenCreated {
                    flow.collect {
                        detailAdapter.submitData(it)
                    }
                }
            }
        }
    }

    private fun runCurrentBitmapCallback(callback: (Bitmap) -> Unit) {
        loadingDialog.show()
        viewModel.getAtPosition(layoutBinding.viewPager.currentItem)?.let { image ->
            lifecycleScope.launch {
                viewModel.getImage(image).let {
                    if(it.isSuccess()) this@DetailBaseActivity.downloadGlideImage(it.data!!).let { result ->
                        if(result.isSuccess()) {
                            rewardVideoManager.showRewardVideo {
                                loadingDialog.dismiss()
                                callback(result.data!!)
                            }
                        }
                        else {
                            loadingDialog.dismiss()
                            toast("Action Failed!")
                        }
                    }
                    else {
                        loadingDialog.dismiss()
                        toast("Action Failed!")
                    }
                    //  if (it.isSuccess()) callback(it.data!!)
                }
            }
        }
    }

    private fun setupClickListeners() {
        layoutBinding.back.setOnClickListener { onBackPressed() }
        layoutBinding.share.setOnClickListener {
            runCurrentBitmapCallback { toolingHandler.shareImage(it) }
        }
        layoutBinding.download.setOnClickListener {
            runCurrentBitmapCallback { toolingHandler.saveImage(it) }
        }
        layoutBinding.wallpaper.setOnClickListener {
            runCurrentBitmapCallback { wallpaperDialog.show() }
        }
        layoutBinding.favorite.setOnClickListener {
            viewModel.getAtPosition(layoutBinding.viewPager.currentItem)?.let {
                it.wallpaper.isFavorite = !it.wallpaper.isFavorite
                setFavorite(it.wallpaper.isFavorite)
                if (it.wallpaper.isFavorite) viewModel.saveFavoriteWallpaper(it)
                else viewModel.removeFavoriteWallpaper(it)
            }
        }
    }

    private fun setupViewPager() {
        layoutBinding.viewPager.adapter = detailAdapter
        layoutBinding.viewPager.setPageTransformer(
            ZoomOutPageTransformer()
        )
        layoutBinding.viewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    interstitialManager?.showAd {
                        viewModel.getAtPosition(position)?.let {
                            setFavorite(it.wallpaper.isFavorite)
                        }
                    }
                    super.onPageSelected(position)
                }
            }
        )
    }

    private fun setFavorite(isFavorite: Boolean) {
        layoutBinding.apply {
            favoriteIcon.setImageResource(
                if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_unfavorite
            )
            favoriteText.text = getString(
                if (isFavorite) R.string.favorite else R.string.un_favorite
            )
        }
    }

    private fun showLayout(isShown: Boolean) {
        layoutBinding.detailLayout.visibility = if (isShown) View.VISIBLE else View.GONE
        layoutBinding.circularProgress.visibility = if (isShown) View.GONE else View.VISIBLE
    }

    private fun setupLoadingIndicator() {
        detailAdapter.addLoadStateListener {
            showLayout(
                it.refresh != LoadState.Loading
            )
        }
    }
}