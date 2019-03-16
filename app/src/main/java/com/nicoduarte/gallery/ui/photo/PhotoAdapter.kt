package com.nicoduarte.gallery.ui.photo

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.nicoduarte.gallery.GlideApp
import com.nicoduarte.gallery.R
import com.nicoduarte.gallery.database.model.Photo
import com.nicoduarte.gallery.inflate
import kotlinx.android.synthetic.main.item_photo.view.*

class PhotoAdapter(var items: MutableList<Photo>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val SPAN_COUNT: Int = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return PhotoHolder(parent.inflate(R.layout.item_photo))
    }

    override fun getItemCount() = items.size


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PhotoHolder).bind(items[position])
    }

    inner class PhotoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(photo: Photo) = with(itemView) {
            tvTitle.text = photo.title
            GlideApp.with(context)
                .load(photo.thumbnailUrl)
                .placeholder(R.drawable.album_placeholder)
                .into(ivPhoto)
        }
    }
}