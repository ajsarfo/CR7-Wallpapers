package com.sarftec.cristianoronaldo.view.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sarftec.cristianoronaldo.R
import com.sarftec.cristianoronaldo.databinding.ActivityUploadBinding
import com.sarftec.cristianoronaldo.utils.Resource
import com.sarftec.cristianoronaldo.view.adapter.WallpaperUploadAdapter
import com.sarftec.cristianoronaldo.view.dialog.WallpaperProgressDialog
import com.sarftec.cristianoronaldo.view.handler.FetchPicturesHandler
import com.sarftec.cristianoronaldo.view.handler.ReadWriteHandler
import com.sarftec.cristianoronaldo.view.utils.toast
import com.sarftec.cristianoronaldo.view.viewmodel.UploadViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UploadActivity : BaseActivity() {

    private val layoutBinding by lazy {
        ActivityUploadBinding.inflate(
            layoutInflater
        )
    }

    private lateinit var fetchPicturesHandler: FetchPicturesHandler

    private lateinit var readWriteHandler: ReadWriteHandler

    private val viewModel by viewModels<UploadViewModel>()

    private val uploadAdapter by lazy {
        WallpaperUploadAdapter(lifecycleScope, viewModel)
    }

    private val categorySections by lazy {
        resources.getStringArray(R.array.category_sections)
    }

    private val wallpaperSections by lazy {
        resources.getStringArray(R.array.wallpapers_remote_sections)
    }

    private var uploadJob: Job? = null

    private val uploadDialog by lazy {
        WallpaperProgressDialog(
            layoutBinding.root,
            this,
            onCancel = {
                uploadJob = null
                toast("Upload failed")
            },
            onFinished = {
                uploadJob = null
                toast("Upload success")
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarBackgroundLight()
        setContentView(layoutBinding.root)
        fetchPicturesHandler = FetchPicturesHandler(this)
        readWriteHandler = ReadWriteHandler(this)
        setupAdapter()
        setupSpinners()
        setupButtons()
        layoutBinding.toolbar.setNavigationOnClickListener { onBackPressed() }
        viewModel.uploadWallpapers.observe(this) {
            uploadAdapter.submitData(it)
            setButtonContainerVisibility(it.isNotEmpty())
        }
        viewModel.updateAdapterPosition.observe(this) { event ->
            event.getIfNotHandled()?.let {
                uploadAdapter.notifyItemChanged(it)
            }
        }
    }

    private fun getWallpapersFromDevice() {
        fetchPicturesHandler.getImagesFromDevice {
            viewModel.setUploadWallpapers(it)
        }
    }

    private fun setButtonContainerVisibility(isVisible: Boolean) {
        layoutBinding.buttonsContainer.visibility = if (isVisible) View.VISIBLE else View.GONE
        layoutBinding.loadWallpapers.visibility = if (isVisible) View.GONE else View.VISIBLE
        layoutBinding.uploadText.visibility = if (isVisible) View.GONE else View.VISIBLE
    }

    private suspend fun uploadWallpaper(
        overlay: UploadViewModel.UploadInfoOverlay
    ): Resource<Unit> {
        return viewModel.uploadWallpaper(
            UploadViewModel.UploadInfo(
                wallpaperSections[layoutBinding.sectionSpinner.selectedIndex],
                categorySections[layoutBinding.categorySpinner.selectedIndex],
                overlay
            )
        )
    }

    private fun uploadWallpapers(items: List<UploadViewModel.UploadInfoOverlay>) {
        if(items.isEmpty()) {
            uploadDialog.dismiss()
            return
        }
        val progressDiff = 1 / items.size.toFloat()
        uploadJob = lifecycleScope.launch {
            val tasks = items.map { overlay ->
                async(Dispatchers.IO) { uploadWallpaper(overlay) }
            }
            tasks.forEachIndexed { index, deferred ->
                val result = deferred.await()
                if (result.isError()) Log.v("TAG", "${result.message}")
                if (result.isSuccess()) {
                    uploadDialog.setProgress((progressDiff * (index + 1) * 100).toInt())
                }
            }
        }
    }

    private fun setupButtons() {
        layoutBinding.loadWallpapers.setOnClickListener {
           readWriteHandler.requestReadWrite {
               getWallpapersFromDevice()
           }
        }
        layoutBinding.upload.setOnClickListener {
            uploadDialog.show()
            uploadWallpapers(viewModel.getUploadWallpapers())
        }
        layoutBinding.clear.setOnClickListener {
            viewModel.clearUploadWallpapers()
        }
    }

    private fun setupSpinners() {
        layoutBinding.categorySpinner.setBackgroundColor(
            ContextCompat.getColor(this, R.color.upload_spinner_background)
        )
        layoutBinding.sectionSpinner.setBackgroundColor(
            ContextCompat.getColor(this, R.color.upload_spinner_background)
        )
        layoutBinding.categorySpinner.setItems(*categorySections)
        layoutBinding.sectionSpinner.setItems(*wallpaperSections)
    }

    private fun setupAdapter() {
        layoutBinding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(
                this@UploadActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = uploadAdapter
        }
    }
}