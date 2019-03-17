package com.nicoduarte.gallery.ui.detail

import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.nicoduarte.gallery.R
import com.nicoduarte.gallery.database.model.Photo
import com.nicoduarte.gallery.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_photo_detail.*

class PhotoDetailActivity : BaseActivity() {

    companion object {
        const val EXTRA_POSITION = "EXTRA_POSITION"
        const val EXTRA_PHOTO_LIST = "EXTRA_PHOTO_LIST"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_detail)

        toolbarToLoad(toolbarView as Toolbar)
        enableHomeDisplay(true)
        setUpViewPager()
    }

    private fun setUpViewPager() {
        val photos = intent?.extras?.getParcelableArrayList<Photo>(EXTRA_PHOTO_LIST)
        vpPhotos.adapter = PhotoPagerAdapter(photos!!)
    }
}
