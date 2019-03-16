package com.nicoduarte.gallery.ui.album

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.nicoduarte.gallery.GalleryRepository

class AlbumViewModel(
    application: Application,
    private val repository: GalleryRepository
) : AndroidViewModel(application) {

}