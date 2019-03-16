package com.nicoduarte.gallery.ui.photo

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.nicoduarte.gallery.GalleryRepository

class ViewModelFactory (
    private val mApplication: Application,
    private val albumId: Int
) : ViewModelProvider.NewInstanceFactory() {

    private val repository: GalleryRepository = GalleryRepository(mApplication)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PhotoViewModel(mApplication, repository, albumId) as T
    }
}