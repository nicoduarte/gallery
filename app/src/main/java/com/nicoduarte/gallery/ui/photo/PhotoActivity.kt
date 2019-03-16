package com.nicoduarte.gallery.ui.photo

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.Toolbar
import com.nicoduarte.gallery.R
import com.nicoduarte.gallery.database.model.Photo
import com.nicoduarte.gallery.ui.BaseActivity
import com.nicoduarte.gallery.utils.ItemOffsetDecoration
import kotlinx.android.synthetic.main.activity_photo.*
import java.util.ArrayList

class PhotoActivity : BaseActivity() {

    companion object {
        const val EXTRA_ID: String = "EXTRA_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)

        toolbarToLoad(toolbarView as Toolbar)
        enableHomeDisplay(true)
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val layoutManager = GridLayoutManager(this, PhotoAdapter.SPAN_COUNT)
        rvPhotoList.layoutManager = layoutManager
        rvPhotoList.adapter = PhotoAdapter(getPhotos())
        rvPhotoList.addItemDecoration(ItemOffsetDecoration(resources.getDimension(R.dimen.item_offset).toInt()))
    }

    private fun getPhotos(): MutableList<Photo> {
        val photos = ArrayList<Photo>()
        photos.add(Photo(1,2,"","",""))
        photos.add(Photo(1,2,"","",""))
        photos.add(Photo(1,2,"","",""))
        photos.add(Photo(1,2,"","",""))
        photos.add(Photo(1,2,"","",""))
        photos.add(Photo(1,2,"","",""))
        photos.add(Photo(1,2,"","",""))
        photos.add(Photo(1,2,"","",""))
        photos.add(Photo(1,2,"","",""))
        photos.add(Photo(1,2,"","",""))
        return photos
    }
}
