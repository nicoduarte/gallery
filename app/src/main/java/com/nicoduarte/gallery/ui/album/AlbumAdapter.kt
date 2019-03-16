package com.nicoduarte.gallery.ui.album

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.nicoduarte.gallery.R
import com.nicoduarte.gallery.database.Album
import com.nicoduarte.gallery.inflate
import kotlinx.android.synthetic.main.item_album.view.*

class AlbumAdapter(var items: MutableList<Album>): RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    companion object {
        const val SPAN_COUNT: Int = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return AlbumHolder(parent.inflate(R.layout.item_album))
    }

    override fun getItemCount() = items.size


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as AlbumHolder).bind(items[position])
    }

    inner class AlbumHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(album: Album) = with(itemView) {
            tvTitle.text = album.title
//            GlideApp.with(context)
//                .load("")
//                .placeholder(R.drawable.album_placeholder)
//                .into(ivAlbum)
        }
    }

    fun addAlbums(movies: List<Album>) {
        val positionStart = items.size
        items.addAll(movies)
        notifyItemRangeInserted(positionStart, items.size)
    }
}