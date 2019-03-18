package com.nicoduarte.gallery.ui.album

import com.nicoduarte.gallery.database.model.Album

data class AlbumState(
    val loading: Boolean = false,
    val albums: List<Album>? = null,
    val error: String? = null
)