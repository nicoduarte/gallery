package com.nicoduarte.gallery.ui.album

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.nicoduarte.gallery.GalleryRepository

class ViewModelFactory (private val mApplication: Application)
    : ViewModelProvider.NewInstanceFactory() {

    private val repository: GalleryRepository = GalleryRepository(mApplication)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AlbumViewModel(mApplication, repository) as T
    }
}