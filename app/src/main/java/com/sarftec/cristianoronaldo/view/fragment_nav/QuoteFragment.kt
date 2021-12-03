package com.sarftec.cristianoronaldo.view.fragment_nav

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.sarftec.cristianoronaldo.databinding.FragmentQuoteBinding
import com.sarftec.cristianoronaldo.view.adapter.QuoteAdapter
import com.sarftec.cristianoronaldo.view.dialog.WallpaperSetDialog
import com.sarftec.cristianoronaldo.view.handler.ToolingHandler
import com.sarftec.cristianoronaldo.view.listener.QuoteFragmentListener
import com.sarftec.cristianoronaldo.view.viewmodel.QuoteViewModel
import com.sarftec.cristianoronaldo.view.viewpager.ZoomOutPageTransformer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QuoteFragment : Fragment() {

    private lateinit var layoutBinding: FragmentQuoteBinding

    private lateinit var listener: QuoteFragmentListener

    private val viewModel by viewModels<QuoteViewModel>()

    private val quoteAdapter by lazy {
        QuoteAdapter(lifecycleScope, viewModel)
    }

    private val toolingHandler by lazy {
        ToolingHandler(requireContext(), listener.getReadWriteHandler())
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

    private fun runCurrentBitmapCallback(callback: (Bitmap) -> Unit) {
        viewModel.getAtPosition(layoutBinding.viewPager.currentItem)?.let { image ->
            lifecycleScope.launch {
                viewModel.getImage(image).let {
                    if (it.isSuccess()) callback(it.data!!)
                }
            }
        }
    }

    private fun revealLayout(isRevealed: Boolean) {
        layoutBinding.circularProgress.visibility = if(!isRevealed) View.VISIBLE else View.GONE
        layoutBinding.quoteLayout.visibility = if(isRevealed) View.VISIBLE else View.GONE
    }

    override fun onAttach(context: Context) {
        if(context is QuoteFragmentListener) listener = context
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.loadQuotes()
        layoutBinding = FragmentQuoteBinding.inflate(
            layoutInflater,
            container,
            false
        )
        setupViewPager()
        setupButtonListeners()
        viewModel.quoteList.observe(viewLifecycleOwner) {
            if(it.isLoading()) revealLayout(false)
            if(it.isSuccess()) {
                revealLayout(true)
                quoteAdapter.submitData(it.data!!)
            }
            if(it.isError()) {
                Log.v("TAG", "${it.message}")
            }
        }
       return layoutBinding.root
    }

    private fun setupButtonListeners() {
        layoutBinding.share.setOnClickListener {
            runCurrentBitmapCallback { toolingHandler.shareImage(it) }
        }
        layoutBinding.download.setOnClickListener {
            runCurrentBitmapCallback { toolingHandler.saveImage(it) }
        }
        layoutBinding.wallpaper.setOnClickListener {
            wallpaperDialog.show()
        }
    }

    private fun setupViewPager() {
        layoutBinding.viewPager.adapter = quoteAdapter
        layoutBinding.viewPager.setPageTransformer(
            ZoomOutPageTransformer()
        )
    }
}