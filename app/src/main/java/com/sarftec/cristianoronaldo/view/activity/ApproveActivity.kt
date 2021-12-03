package com.sarftec.cristianoronaldo.view.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.sarftec.cristianoronaldo.R
import com.sarftec.cristianoronaldo.databinding.ActivityApproveBinding
import com.sarftec.cristianoronaldo.domain.model.CR7Wallpaper
import com.sarftec.cristianoronaldo.utils.Resource
import com.sarftec.cristianoronaldo.view.adapter.ApproveAdapter
import com.sarftec.cristianoronaldo.view.dialog.LoadingDialog
import com.sarftec.cristianoronaldo.view.dialog.WallpaperProgressDialog
import com.sarftec.cristianoronaldo.view.utils.toast
import com.sarftec.cristianoronaldo.view.viewmodel.ApproveViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ApproveActivity : BaseActivity() {

    private val layoutBinding by lazy {
        ActivityApproveBinding.inflate(
            layoutInflater
        )
    }

    private val viewModel by viewModels<ApproveViewModel>()

    private val approveAdapter by lazy {
        ApproveAdapter(lifecycleScope, viewModel)
    }

    private var approveJob: Job? = null

    private val progressDialog by lazy {
        WallpaperProgressDialog(
            layoutBinding.root,
            this,
            onCancel = {
                approveJob?.cancel()
                toast("Upload failed")
            },
            onFinished = {
                approveJob = null
                toast("Upload success")
            }
        )
    }

    private val loadingDialog by lazy {
        LoadingDialog(this, layoutBinding.root)
    }

    /*
    private val categorySections by lazy {
        resources.getStringArray(R.array.category_sections)
    }
     */

    private val wallpaperSections by lazy {
        resources.getStringArray(R.array.wallpapers_remote_sections)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarBackgroundLight()
        setContentView(layoutBinding.root)
        setupSpinners()
        setupAdapter()
        setupLoadAdapter()
        setupButtonListeners()
        setRecyclerVisibility(false)
        setupToolbar()
        viewModel.wallpaperFlow.observe(this) {
            if (it.isSuccess()) lifecycleScope.launch {
                it.data!!.collect { pagingData ->
                    approveAdapter.submitData(pagingData)
                }
            }
        }
        viewModel.adapterUpdate.observe(this) { event ->
            event.getIfNotHandled()?.let {
                approveAdapter.updateViewHolder(it)
            }
        }
    }

    private fun setupToolbar() {
        layoutBinding.toolbar.setNavigationOnClickListener { onBackPressed() }
        layoutBinding.toolbar.setOnMenuItemClickListener {
            if(it.itemId == R.id.reset) {
                setRecyclerVisibility(false)
                true
            }
            else false
        }
    }

    private fun setupSpinners() {
       /*
        layoutBinding.categorySpinner.setBackgroundColor(
            ContextCompat.getColor(this, R.color.upload_spinner_background)
        )
        */
        layoutBinding.sectionSpinner.setBackgroundColor(
            ContextCompat.getColor(this, R.color.upload_spinner_background)
        )
       /*
        layoutBinding.categorySpinner.setItems(*categorySections)
        */
        layoutBinding.sectionSpinner.setItems(*wallpaperSections)
    }

    private fun setRecyclerVisibility(isVisible: Boolean) {
        layoutBinding.apply {
            selectionSize.visibility = if (isVisible) View.VISIBLE else View.GONE
            recyclerView.visibility = if (isVisible) View.VISIBLE else View.GONE
            buttonsContainer.visibility = if (isVisible) View.VISIBLE else View.GONE
            loadWallpapers.visibility = if (isVisible) View.GONE else View.VISIBLE
            uploadText.visibility = if(isVisible) View.GONE else View.VISIBLE
        }
    }

    private fun approveWallpapers(wallpapers: List<CR7Wallpaper>) {
        wallpaperAction(
            wallpapers,
            getAction = { viewModel.approveWallpaper(it) },
            approveAction = { viewModel.approveWallpaper(it) }
        )
    }

    private fun deleteWallpapers(wallpapers: List<CR7Wallpaper>) {
        wallpaperAction(
            wallpapers,
            getAction = { viewModel.deleteWallpaper(it) },
            approveAction = { viewModel.deleteWallpaper(it) }
        )
    }

    private fun wallpaperAction(
        wallpapers: List<CR7Wallpaper>,
        getAction: suspend (CR7Wallpaper) -> Resource<Unit>,
        approveAction: (String) -> Unit
    ) {
        if (wallpapers.isEmpty()) {
            progressDialog.dismiss()
            return
        }
        val progressDiff = 1 / wallpapers.size.toFloat()
        approveJob = lifecycleScope.launch {
            val tasks = wallpapers.map { wallpaper ->
                async(Dispatchers.IO) {
                    getAction(wallpaper).let {
                        if (it.isSuccess()) Resource.success(wallpaper.id.toString())
                        else Resource.error("${it.message}")
                    }
                }
            }
            tasks.forEachIndexed { index, deferred ->
                val result = deferred.await()
                if (result.isError()) Log.v("TAG", "${result.message}")
                if (result.isSuccess()) {
                    approveAction(result.data!!)
                    progressDialog.setProgress((progressDiff * (index + 1) * 100).toInt())
                }
            }
        }
    }

    private fun setupButtonListeners() {
        layoutBinding.delete.setOnClickListener {
            progressDialog.show()
            approveWallpapers(viewModel.getSelectedWallpapers())
        }
        layoutBinding.clear.setOnClickListener {
            viewModel.clearWallpapers()
        }
        layoutBinding.delete.setOnClickListener {
            deleteWallpapers(viewModel.getSelectedWallpapers())
        }
        layoutBinding.loadWallpapers.setOnClickListener {
            loadingDialog.show()
            setRecyclerVisibility(true)
            viewModel.loadWallpapers()
        }
    }

    private fun setupLoadAdapter() {
        approveAdapter.addLoadStateListener {
            if(it.refresh !is LoadState.Loading) loadingDialog.dismiss()
        }
    }

    private fun setupAdapter() {
        layoutBinding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(
                this@ApproveActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = approveAdapter
        }
    }
}