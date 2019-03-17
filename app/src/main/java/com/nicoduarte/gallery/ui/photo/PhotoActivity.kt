package com.nicoduarte.gallery.ui.photo

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import com.nicoduarte.gallery.R
import com.nicoduarte.gallery.database.model.Photo
import com.nicoduarte.gallery.gone
import com.nicoduarte.gallery.ui.BaseActivity
import com.nicoduarte.gallery.ui.detail.PhotoDetailActivity
import com.nicoduarte.gallery.utils.ItemOffsetDecoration
import com.nicoduarte.gallery.visible
import kotlinx.android.synthetic.main.activity_photo.*

class PhotoActivity : BaseActivity() {

    companion object {
        const val EXTRA_ID: String = "EXTRA_ID"
    }

    private lateinit var viewModel: PhotoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)

        toolbarToLoad(toolbarView as Toolbar)
        enableHomeDisplay(true)
        setUpRecyclerView()
        setUpViewModel()
    }

    private fun setUpViewModel() {
        val albumId = intent?.extras?.get(EXTRA_ID) as Int
        viewModel = ViewModelFactory(application, albumId).create(PhotoViewModel::class.java)
        viewModel.getPhotoState().observe(this, Observer { it?.let {update(it)} })
        viewModel.getPhotos()
    }

    private fun setUpRecyclerView() {
        val layoutManager = GridLayoutManager(this, PhotoAdapter.SPAN_COUNT)
        rvPhotoList.layoutManager = layoutManager
        rvPhotoList.addItemDecoration(ItemOffsetDecoration(resources.getDimension(R.dimen.item_offset).toInt()))
    }

    private fun update(state: PhotoState) {
        updateProgressBar(state)
        checkError(state)
        showPhotoList(state)
    }

    private fun showPhotoList(state: PhotoState) {
        if (state.photos != null) {
            if (rvPhotoList.adapter == null) {
                val adapter = PhotoAdapter(state.photos.toMutableList()) { position -> launchActivity(position,
                    state.photos.toMutableList() as ArrayList<Photo>
                )}
                rvPhotoList.adapter = adapter
            }
        }
    }

    private fun launchActivity(position: Int, photos: ArrayList<Photo>) {
        val intent = Intent(this, PhotoDetailActivity::class.java)
        intent.putExtra(PhotoDetailActivity.EXTRA_POSITION, position)
        intent.putParcelableArrayListExtra(PhotoDetailActivity.EXTRA_PHOTO_LIST, photos)
        startActivity(intent)
    }

    private fun checkError(state: PhotoState) {
        val parentLayout = findViewById<View>(android.R.id.content)
        state.error?.let { Snackbar.make(parentLayout, state.error, Snackbar.LENGTH_SHORT).show()  }
    }

    private fun updateProgressBar(state: PhotoState) {
        when {
            state.loading -> pbPhoto.visible()
            else -> pbPhoto.gone()
        }
    }
}
