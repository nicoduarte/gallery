package com.nicoduarte.gallery.ui.album

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.Toolbar
import com.nicoduarte.gallery.R
import com.nicoduarte.gallery.database.Album
import com.nicoduarte.gallery.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_album.*

class AlbumActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)

        toolbarToLoad(toolbarView as Toolbar)
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val layoutManager = GridLayoutManager(this, 2)
        rvAlbumList.layoutManager = layoutManager
        rvAlbumList.adapter = AlbumAdapter(getAlbums())
    }

    fun getAlbums(): ArrayList<Album> {
        val albums = ArrayList<Album>()
        albums.add(Album(1,1,"asdasdasd"))
        albums.add(Album(1,1,"asdasdasd"))
        albums.add(Album(1,1,"asdasdasd"))
        albums.add(Album(1,1,"asdasdasd"))
        albums.add(Album(1,1,"asdasdasd"))
        return albums
    }
}
