package com.nicoduarte.gallery

import android.app.Application
import com.nicoduarte.gallery.api.ApiService
import com.nicoduarte.gallery.database.model.Album
import io.reactivex.Single

class GalleryRepository(application: Application) {

    fun getAlbums(page: Int): Single<List<Album>> {
        return getAlbumsFromServer(page)
//            .map { it.albums }
//            .flatMapIterable { it }
//            .map { it.copy(category = category.value) }
//            .toList()
//            .toObservable()
//            .onErrorResumeNext { t: Throwable ->  getAlbumsFromDb(category)}
//            .doOnNext { albumDao.insertAll(it) }
    }

    private fun getAlbumsFromServer(page: Int): Single<List<Album>> {
        return ApiService.getInstance().getAlbums(page)
    }
}