package com.nicoduarte.gallery.ui.detail

import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.nicoduarte.gallery.GlideApp
import com.nicoduarte.gallery.R
import com.nicoduarte.gallery.database.model.Photo

class PhotoPagerAdapter(private val photoList: ArrayList<Photo>) : PagerAdapter() {

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }

    override fun getCount() = photoList.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.item_photo_detail, container, false)

        val ivPhoto = view.findViewById<ImageView>(R.id.ivPhotoDetail)

        val photoItem = photoList[position]
        view.findViewById<TextView>(R.id.tvTitle).text = photoItem.title

        GlideApp.with(container.context)
            .load(photoItem.url)
            .placeholder(R.drawable.album_placeholder)
            .into(ivPhoto)

        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }
}