package com.nicoduarte.gallery.api

import com.nicoduarte.gallery.database.model.Album
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiRequest {

    @GET("albums?_embed=photos")
    fun getAlbums(@Query("_page") page: Int): Single<List<Album>>
}