package com.nicoduarte.gallery.ui.album

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import com.nicoduarte.gallery.R
import com.nicoduarte.gallery.database.model.Album
import com.nicoduarte.gallery.gone
import com.nicoduarte.gallery.ui.BaseActivity
import com.nicoduarte.gallery.ui.photo.PhotoActivity
import com.nicoduarte.gallery.utils.EndlessRecyclerViewScrollListener
import com.nicoduarte.gallery.utils.ItemOffsetDecoration
import com.nicoduarte.gallery.visible
import jp.wasabeef.recyclerview.animators.ScaleInAnimator
import kotlinx.android.synthetic.main.activity_album.*

class AlbumActivity : BaseActivity() {

    companion object {
        const val DURATION: Long = 500
    }

    private lateinit var viewModel: AlbumViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)

        toolbarToLoad(toolbarView as Toolbar)
        setUpRecyclerView()
        setUpViewModel()
    }

    private fun setUpViewModel() {
        viewModel = ViewModelFactory(application).create(AlbumViewModel::class.java)
        viewModel.getAlbumState().observe(this, Observer { it?.let {update(it)} })
        viewModel.getAlbums()
    }

    private fun setUpRecyclerView() {
        val layoutManager = GridLayoutManager(this, AlbumAdapter.SPAN_COUNT)
        rvAlbumList.layoutManager = layoutManager
        rvAlbumList.itemAnimator = ScaleInAnimator(AccelerateDecelerateInterpolator())
        rvAlbumList.itemAnimator!!.addDuration = DURATION
        val adapter = AlbumAdapter(emptyList<Album>().toMutableList()) { id: Int -> launchPhotoActivity(id) }
        rvAlbumList.adapter = adapter
        rvAlbumList.addItemDecoration(ItemOffsetDecoration(resources.getDimension(R.dimen.item_offset).toInt()))
        rvAlbumList.addOnScrollListener(
            object : EndlessRecyclerViewScrollListener(rvAlbumList.layoutManager as GridLayoutManager) {

                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                    viewModel.getAlbums(page)
                }
            })
    }

    private fun update(state: AlbumState) {
        updateProgressBar(state)
        checkError(state)
        showAlbumList(state)
    }

    private fun showAlbumList(state: AlbumState) {
        if (state.albums != null) {
            if (rvAlbumList.adapter == null) {
                val adapter = AlbumAdapter(state.albums.toMutableList()) { id: Int -> launchPhotoActivity(id) }
                rvAlbumList.adapter = adapter
            } else {
                val adapter = rvAlbumList.adapter as? AlbumAdapter
                adapter?.addAlbums(state.albums)
            }
        }
    }

    private fun launchPhotoActivity(id: Int) {
        val intent = Intent(this, PhotoActivity::class.java)
        intent.putExtra(PhotoActivity.EXTRA_ID, id)
        startActivity(intent)
    }

    private fun checkError(state: AlbumState) {
        val parentLayout = findViewById<View>(android.R.id.content)
        state.error?.let { Snackbar.make(parentLayout, state.error, Snackbar.LENGTH_SHORT).show()  }
    }

    private fun updateProgressBar(state: AlbumState) {
        when {
            state.loading -> pbAlbum.visible()
            else -> pbAlbum.gone()
        }
    }
}
