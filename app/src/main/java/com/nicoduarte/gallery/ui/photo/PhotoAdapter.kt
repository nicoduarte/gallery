package com.nicoduarte.gallery.ui.photo

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.nicoduarte.gallery.GlideApp
import com.nicoduarte.gallery.R
import com.nicoduarte.gallery.database.model.Photo
import com.nicoduarte.gallery.inflate
import kotlinx.android.synthetic.main.item_photo.view.*

class PhotoAdapter(var items: MutableList<Photo>, private val clickListener: (position: Int, items: List<Photo>, imageShared: View) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val SPAN_COUNT: Int = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return PhotoHolder(parent.inflate(R.layout.item_photo))
    }

    override fun getItemCount() = items.size


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PhotoHolder).bind(items[position], clickListener)
    }

    inner class PhotoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            photo: Photo,
            clickListener: (position: Int, items: List<Photo>, imageShared: View) -> Unit
        ) = with(itemView) {
            tvTitle.text = photo.title
            GlideApp.with(context)
                .load(photo.thumbnailUrl)
                .placeholder(R.drawable.album_placeholder)
                .into(ivPhoto)
            ivPhoto.transitionName = adapterPosition.toString()
            setOnClickListener { clickListener(adapterPosition, items.toList(), ivPhoto) }
        }
    }

    fun addAlbums(movies: List<Photo>) {
        val positionStart = items.size
        items.addAll(movies)
        notifyItemRangeInserted(positionStart, items.size)
    }
}