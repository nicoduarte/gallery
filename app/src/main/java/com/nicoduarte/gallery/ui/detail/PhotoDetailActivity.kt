package com.nicoduarte.gallery.ui.detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.SharedElementCallback
import android.support.v7.widget.Toolbar
import android.view.View
import com.github.chrisbanes.photoview.PhotoView
import com.nicoduarte.gallery.R
import com.nicoduarte.gallery.database.model.Photo
import com.nicoduarte.gallery.ui.BaseActivity
import com.nicoduarte.gallery.utils.DepthTransformation
import kotlinx.android.synthetic.main.activity_photo_detail.*


class PhotoDetailActivity : BaseActivity() {

    companion object {
        const val EXTRA_POSITION = "EXTRA_POSITION"
        const val EXTRA_PHOTO_LIST = "EXTRA_PHOTO_LIST"
        const val EXIT_POSITION = "EXIT_POSITION"
    }

    private var currentItem: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
        setContentView(R.layout.activity_photo_detail)

        window.statusBarColor = resources.getColor(android.R.color.black, theme)

        toolbarView.setBackgroundColor(resources.getColor(R.color.transparent_black, theme))
        toolbarToLoad(toolbarView as Toolbar)
        enableHomeDisplay(true)
        setUpViewPager()
    }

    private fun setUpViewPager() {
        val photos = intent?.extras?.getParcelableArrayList<Photo>(EXTRA_PHOTO_LIST)
        currentItem = intent?.getIntExtra(EXTRA_POSITION, 0)!!
        vpPhotos.adapter = PhotoPagerAdapter(photos!!, currentItem) { finishAfterTransition() }
        vpPhotos.setPageTransformer(true, DepthTransformation())
        vpPhotos.currentItem = currentItem
    }

    override fun finishAfterTransition() {
        val pos = vpPhotos.currentItem
        val intent = Intent()
        intent.putExtra(EXIT_POSITION, pos)
        setResult(Activity.RESULT_OK, intent)
        if (currentItem != pos) {
            val view = vpPhotos.findViewWithTag<View>(pos.toString())
            val sharedView = view.findViewById<PhotoView>(R.id.ivPhotoDetail)
            setSharedElementCallback(sharedView)
        }
        super.finishAfterTransition()
    }

    private fun setSharedElementCallback(view: View) {
        setEnterSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(names: MutableList<String>, sharedElements: MutableMap<String, View>) {

                // Map the first shared element name to the child ImageView.
                sharedElements[names[0]] = view
            }
        })
    }
}
