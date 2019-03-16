package com.nicoduarte.gallery.ui.album

import com.nicoduarte.gallery.database.Album

data class AlbumState(
    val loading: Boolean = true,
    val albums: List<Album>? = null,
    val error: String? = null
)