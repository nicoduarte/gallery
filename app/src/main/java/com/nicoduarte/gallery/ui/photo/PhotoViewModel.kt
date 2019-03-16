package com.nicoduarte.gallery.ui.photo

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.nicoduarte.gallery.GalleryRepository
import com.nicoduarte.gallery.api.ApiErrorParser
import com.nicoduarte.gallery.database.model.Photo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PhotoViewModel(
    application: Application,
    private val repository: GalleryRepository,
    private val albumId: Int
): AndroidViewModel(application) {

    private val photoState: MutableLiveData<PhotoState> = MutableLiveData()
    private val compositeDisposable: CompositeDisposable
    private val apiErrorParser: ApiErrorParser

    init {
        photoState.value = PhotoState()
        compositeDisposable = CompositeDisposable()
        apiErrorParser = ApiErrorParser(application)
    }

    fun getPhotoState(): LiveData<PhotoState> = photoState

    fun getPhotos() {
        val disposable = repository.getPhotosByAlbumId(albumId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(this::onSuccessPhotos, this::onErrorPhotos)

        compositeDisposable.add(disposable)
    }

    private fun onSuccessPhotos(photos: List<Photo>) {
        photoState.value = currentViewState().copy(loading = false, photos = photos, error = null)
    }


    private fun onErrorPhotos(error: Throwable?) {
        var message: String? = "Unknown error"
        error?.let { message = apiErrorParser.parseError(error) }
        photoState.value = currentViewState().copy(loading = false, error = message)
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    private fun currentViewState(): PhotoState = photoState.value!!
}