package com.nicoduarte.gallery.ui.album

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.nicoduarte.gallery.GalleryRepository
import com.nicoduarte.gallery.api.ApiErrorParser
import com.nicoduarte.gallery.database.model.Album
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class AlbumViewModel(
    application: Application,
    private val repository: GalleryRepository
) : AndroidViewModel(application) {

    private val albumState: MutableLiveData<AlbumState> = MutableLiveData()
    private val compositeDisposable: CompositeDisposable
    private val apiErrorParser: ApiErrorParser

    init {
        albumState.value = AlbumState()
        compositeDisposable = CompositeDisposable()
        apiErrorParser = ApiErrorParser(application)
    }

    fun getAlbumState(): LiveData<AlbumState> = albumState

    fun getAlbums(page: Int = 1) {
        val disposable = repository.getAlbums(page)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(this::onSuccessAlbums, this::onErrorAlbums)

        compositeDisposable.add(disposable)
    }

    private fun onSuccessAlbums(albums: List<Album>) {
        albumState.value = currentViewState().copy(loading = false, albums = albums, error = null)
    }


    private fun onErrorAlbums(error: Throwable?) {
        var message: String? = "Unknown error"
        error?.let { message = apiErrorParser.parseError(error) }
        albumState.value = currentViewState().copy(loading = false, error = message)
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    private fun currentViewState(): AlbumState = albumState.value!!
}