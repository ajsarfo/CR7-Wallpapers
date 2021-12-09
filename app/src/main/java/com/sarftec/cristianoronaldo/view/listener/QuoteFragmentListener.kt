package com.sarftec.cristianoronaldo.view.listener

import com.sarftec.cristianoronaldo.view.advertisement.InterstitialManager
import com.sarftec.cristianoronaldo.view.advertisement.RewardVideoManager
import com.sarftec.cristianoronaldo.view.handler.ReadWriteHandler

interface QuoteFragmentListener {
    fun getReadWriteHandler() : ReadWriteHandler
    fun showLoadingDialog(isShown: Boolean)
    fun getRewardVideo() : RewardVideoManager
    fun getAdInterstitialManager() : InterstitialManager?
}