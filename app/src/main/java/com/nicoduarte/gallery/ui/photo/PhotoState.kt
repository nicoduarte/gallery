package com.nicoduarte.gallery.ui.photo

import com.nicoduarte.gallery.database.model.Photo

data class PhotoState(
    val loading: Boolean = true,
    val photos: List<Photo>? = null,
    val error: String? = null
)