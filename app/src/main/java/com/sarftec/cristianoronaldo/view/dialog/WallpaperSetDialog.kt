package com.sarftec.cristianoronaldo.view.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.sarftec.cristianoronaldo.databinding.LayoutWallpaperSetDialogBinding

class WallpaperSetDialog(
    parent: ViewGroup,
    private val onHome: () -> Unit,
    private val onLock: () -> Unit
) : AlertDialog(parent.context) {

    private val layoutBinding = LayoutWallpaperSetDialogBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false
    )

    init {
        layoutBinding.apply {
            lockScreen.setOnClickListener {
                onLock()
                dismiss()
            }
            homeScreen.setOnClickListener {
                onHome()
                dismiss()
            }
        }
        setView(layoutBinding.root)
        setCancelable(true)
    }
}