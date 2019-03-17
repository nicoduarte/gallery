package com.nicoduarte.gallery

import android.app.Application
import com.nicoduarte.gallery.api.ApiService
import com.nicoduarte.gallery.database.GalleryDao
import com.nicoduarte.gallery.database.GalleryDatabase
import com.nicoduarte.gallery.database.model.Album
import com.nicoduarte.gallery.database.model.Photo
import io.reactivex.Single
import io.reactivex.SingleSource

class GalleryRepository(application: Application) {

    private val galleryDao: GalleryDao

    init {
        val galleryDatabase = GalleryDatabase.getDatabase(application)
        galleryDao = galleryDatabase.galleryDao()
    }

    fun getAlbums(page: Int): Single<List<Album>> {
        return getAlbumsFromServer(page)
            .onErrorResumeNext { t: Throwable ->  getAlbumsFromDb()}
            .doOnSuccess { galleryDao.insertAlbums(it) }
    }

    // get query by page
    private fun getAlbumsFromDb(): SingleSource<out List<Album>>? {
        return galleryDao.getAlbums()
    }

    // API return empty photos
    private fun getAlbumsFromServer(page: Int): Single<List<Album>> {
        return ApiService.getInstance().getAlbums(page)
    }

    fun getPhotosByAlbumId(albumId: Int): Single<List<Photo>> {
        return getPhotosFromServer(albumId)
            .onErrorResumeNext { t: Throwable ->  getPhotosFromDb(albumId)}
            .doOnSuccess { galleryDao.insertPhotos(it) }
    }

    private fun getPhotosFromDb(albumId: Int): SingleSource<out List<Photo>>? {
        return galleryDao.getPhotos(albumId)
    }

    private fun getPhotosFromServer(albumId: Int): Single<List<Photo>> {
        return ApiService.getInstance().getPhotos(albumId)
    }
}