package com.sarftec.cristianoronaldo.view.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.sarftec.cristianoronaldo.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class SplashActivity : BaseActivity() {

    private val layoutBinding by lazy {
        ActivitySplashBinding.inflate(
            layoutInflater
        )
    }

    override fun canShowInterstitial(): Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statusColor(Color.BLACK)
        setContentView(layoutBinding.root)
        layoutBinding.splashIcon.setImageBitmap(
            getSplashImage()
        )
        lifecycleScope.launchWhenCreated {
            delay(TimeUnit.SECONDS.toMillis(3))
            navigateTo(MainActivity::class.java, finish = true)
        }
    }

    private fun getSplashImage() : Bitmap {
        return assets.open("logo.jpg").use {
            BitmapFactory.decodeStream(it)
        }
    }
}