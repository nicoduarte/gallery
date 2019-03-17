package com.nicoduarte.gallery.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.nicoduarte.gallery.database.model.Album
import com.nicoduarte.gallery.database.model.Photo

@Database(entities = [Album::class, Photo::class], version = 1)
abstract class GalleryDatabase : RoomDatabase() {

    abstract fun galleryDao(): GalleryDao

    companion object {

        private var INSTANCE: GalleryDatabase? = null
        private const val DATABASSE_NAME: String = "gallery_database"

        fun getDatabase(context: Context): GalleryDatabase {
            if (INSTANCE == null) {
                synchronized(GalleryDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            GalleryDatabase::class.java, DATABASSE_NAME
                        ).build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}
