package com.nicoduarte.gallery.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.nicoduarte.gallery.database.model.Album
import com.nicoduarte.gallery.database.model.Photo
import io.reactivex.Single

@Dao
interface GalleryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAlbums(albums: List<Album>)

    @Query("SELECT * from album_table")
    fun getAlbums(): Single<List<Album>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhotos(photos: List<Photo>)

    @Query("SELECT * from photo_table WHERE albumId = :albumId")
    fun getPhotos(albumId: Int): Single<List<Photo>>

}
