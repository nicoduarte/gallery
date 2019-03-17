package com.nicoduarte.gallery.ui.detail

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.v4.app.ActivityCompat.startPostponedEnterTransition
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.github.chrisbanes.photoview.PhotoView
import com.github.chuross.flinglayout.FlingLayout
import com.github.chuross.flinglayout.PositionChangeListener
import com.nicoduarte.gallery.GlideApp
import com.nicoduarte.gallery.R
import com.nicoduarte.gallery.database.model.Photo


class PhotoPagerAdapter(private val photoList: ArrayList<Photo>, val currentItem: Int, val dismissListener: () -> Unit) : PagerAdapter() {

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }

    override fun getCount() = photoList.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.item_photo_detail, container, false)

        val title = view.findViewById<TextView>(R.id.tvTitle)

        val flingLayout = view.findViewById<FlingLayout>(R.id.flingLayout)
        flingLayout.dismissListener = dismissListener

        flingLayout.positionChangeListener = object : PositionChangeListener {
            override fun invoke(top: Int, left: Int, positionRangeRate: Float) {
                flingLayout.setBackgroundColor(Color.argb(Math.round(255 * (1.0F - positionRangeRate)), 0, 0, 0))
                title.alpha = 1.0F - positionRangeRate
            }
        }

        val ivPhoto = view.findViewById<PhotoView>(R.id.ivPhotoDetail)
        ivPhoto.setOnScaleChangeListener { scaleFactor, focusX, focusY ->
            flingLayout.isDragEnabled = (scaleFactor <= 1f)
        }

        ivPhoto.transitionName = position.toString()

        val photoItem = photoList[position]
        title.text = photoItem.title

        GlideApp.with(container.context)
            .load(photoItem.url)
            .dontAnimate()
            .placeholder(R.drawable.album_placeholder)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(p0: GlideException?, p1: Any?, p2: com.bumptech.glide.request.target.Target<Drawable>?, p3: Boolean): Boolean {
                    if (position == currentItem) {
                        startPostponedEnterTransition(container.context as Activity)
                    }
                    return false
                }
                override fun onResourceReady(p0: Drawable?, p1: Any?, p2: com.bumptech.glide.request.target.Target<Drawable>?, p3: DataSource?, p4: Boolean): Boolean {
                    if (position == currentItem) {
                        startPostponedEnterTransition(container.context as Activity)
                    }
                    return false
                }
            })
            .into(ivPhoto)

        view.tag = position.toString()
        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }
}