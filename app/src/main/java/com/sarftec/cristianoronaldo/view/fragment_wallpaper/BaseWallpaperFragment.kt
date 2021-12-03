package com.sarftec.cristianoronaldo.view.fragment_wallpaper

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.GridLayoutManager
import com.sarftec.cristianoronaldo.databinding.LayoutWallpapersBaseBinding
import com.sarftec.cristianoronaldo.view.adapter.viewholder.wallpaper.BaseViewHolder
import com.sarftec.cristianoronaldo.view.listener.WallpaperFragmentListener
import com.sarftec.cristianoronaldo.view.model.WallpaperUI
import com.sarftec.cristianoronaldo.view.viewmodel.WallpapersViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect

abstract class BaseWallpaperFragment : Fragment() {

    protected lateinit var layoutBinding: LayoutWallpapersBaseBinding

    protected var wallpaperFragmentListener: WallpaperFragmentListener? = null

    val viewModel by viewModels<WallpapersViewModel>()

    protected abstract fun getSelectionType(): WallpapersViewModel.Selection

    protected abstract val wallpaperAdapter : PagingDataAdapter<WallpaperUI, BaseViewHolder>

    override fun onAttach(context: Context) {
        if (context is WallpaperFragmentListener) wallpaperFragmentListener = context
        super.onAttach(context)
    }

    @InternalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layoutBinding = LayoutWallpapersBaseBinding.inflate(
            layoutInflater,
            container,
            false
        )
        viewModel.loadWallpapers(getSelectionType())
        setupRecyclerView()
        setupLoadingState()
        observeWallpaperFlow()
        return layoutBinding.root
    }

    @InternalCoroutinesApi
    private fun observeWallpaperFlow() {
        viewModel.wallpaperFlow.observe(viewLifecycleOwner) { resources ->
            lifecycleScope.launchWhenCreated {
                if (resources.isSuccess()) resources.data?.collect {
                    wallpaperAdapter.submitData(it)
                }
                if (resources.isError()) Log.v("TAG", "Resource Error => ${resources.message}")
            }
        }
    }

    private fun setupLoadingState() {
        wallpaperAdapter.addLoadStateListener {
            showLayout(
                it.refresh != LoadState.Loading
            )
        }
    }

    private fun setupRecyclerView() {
        layoutBinding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = wallpaperAdapter
        }
    }

    private fun showLayout(isShown: Boolean) {
        layoutBinding.recyclerView.visibility = if (isShown) View.VISIBLE else View.GONE
        layoutBinding.circularProgress.visibility = if (isShown) View.GONE else View.VISIBLE
    }
}