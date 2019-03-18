package com.nicoduarte.gallery.ui.photo

import android.app.ActivityOptions
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import com.nicoduarte.gallery.R
import com.nicoduarte.gallery.database.model.Photo
import com.nicoduarte.gallery.gone
import com.nicoduarte.gallery.ui.BaseActivity
import com.nicoduarte.gallery.ui.detail.PhotoDetailActivity
import com.nicoduarte.gallery.utils.ItemOffsetDecoration
import com.nicoduarte.gallery.visible
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.android.synthetic.main.activity_photo.*
import android.app.Activity
import android.support.v4.app.SharedElementCallback
import android.view.Menu
import android.view.MenuItem
import com.nicoduarte.gallery.ui.detail.PhotoDetailActivity.Companion.EXIT_POSITION
import android.view.View.OnLayoutChangeListener


class PhotoActivity : BaseActivity() {

    companion object {
        const val EXTRA_ID: String = "EXTRA_ID"
        const val DURATION: Long = 500
        const val TWO_COLUMNS: Int = 2
        const val FOUR_COLUMNS: Int = 4
    }

    private lateinit var viewModel: PhotoViewModel
    private var enterPosition: Int = 0
    private var exitPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)

        toolbarToLoad(toolbarView as Toolbar)
        enableHomeDisplay(true)
        setUpRecyclerView()
        setUpViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.grid -> {
                val gridLayout = rvPhotoList.layoutManager as GridLayoutManager
                if(gridLayout.spanCount == TWO_COLUMNS) {
                    gridLayout.spanCount = FOUR_COLUMNS
                    item.setIcon(R.drawable.ic_two_columns)
                } else {
                    gridLayout.spanCount = TWO_COLUMNS
                    item.setIcon(R.drawable.ic_four_columns)
                }
                rvPhotoList.layoutManager = gridLayout
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setUpViewModel() {
        val albumId = intent?.extras?.get(EXTRA_ID) as Int
        viewModel = ViewModelFactory(application, albumId).create(PhotoViewModel::class.java)
        viewModel.getPhotoState().observe(this, Observer { it?.let {update(it)} })
        viewModel.getPhotos()
    }

    private fun setUpRecyclerView() {
        val layoutManager = GridLayoutManager(this, FOUR_COLUMNS)
        rvPhotoList.layoutManager = layoutManager
        val adapter = PhotoAdapter(emptyList<Photo>().toMutableList()) {
                position: Int, photos: List<Photo>, imageShared: View ->
            launchActivity(position, photos as ArrayList<Photo>, imageShared)
        }
        rvPhotoList.itemAnimator = SlideInUpAnimator(AccelerateDecelerateInterpolator())
        rvPhotoList.itemAnimator!!.addDuration = DURATION
        rvPhotoList.adapter = adapter
//        rvPhotoList.addItemDecoration(ItemOffsetDecoration(resources.getDimension(R.dimen.item_offset).toInt()))
    }

    private fun update(state: PhotoState) {
        updateProgressBar(state)
        checkError(state)
        showPhotoList(state)
    }

    private fun showPhotoList(state: PhotoState) {
        if (state.photos != null) {
            if (rvPhotoList.adapter == null) {
                val adapter = PhotoAdapter(state.photos.toMutableList()) {
                        position: Int, photos: List<Photo>, imageShared: View ->
                    launchActivity(position, photos as ArrayList<Photo>, imageShared)
                }
                rvPhotoList.adapter = adapter
            }  else {
                val adapter = rvPhotoList.adapter as? PhotoAdapter
                adapter?.addAlbums(state.photos)
            }
        }
    }

    private fun launchActivity(
        position: Int,
        photos: ArrayList<Photo>,
        imageShared: View
    ) {
        val intent = Intent(this, PhotoDetailActivity::class.java)
        intent.putExtra(PhotoDetailActivity.EXTRA_POSITION, position)
        intent.putParcelableArrayListExtra(PhotoDetailActivity.EXTRA_PHOTO_LIST, photos)

        val options = ActivityOptions.makeSceneTransitionAnimation(
            this, imageShared, imageShared.transitionName
        )
        startActivityForResult(intent, 0, options.toBundle())
        setCallback(position)
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

    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        super.onActivityReenter(resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            exitPosition = data.getIntExtra(EXIT_POSITION, enterPosition)
            scrollToPosition()
        }
    }

    private fun setCallback(enterPosition: Int) {
        this.enterPosition = enterPosition
        setExitSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(names: MutableList<String>, sharedElements: MutableMap<String, View>) {

                // Locate the ViewHolder for the clicked position.
                val selectedViewHolder = rvPhotoList
                    .findViewHolderForAdapterPosition(exitPosition)
                if (selectedViewHolder?.itemView == null) {
                    return
                }

                // Map the first shared element name to the child ImageView.
                sharedElements[names[0]] = selectedViewHolder.itemView.findViewById(R.id.ivPhoto)
                setExitSharedElementCallback(null as SharedElementCallback?)
            }
        })
    }

    /**
     * Scrolls the recycler view to show the last viewed item in the grid. This is important when
     * navigating back from the grid.
     */
    private fun scrollToPosition() {
        rvPhotoList.addOnLayoutChangeListener(object : OnLayoutChangeListener {
            override fun onLayoutChange(
                v: View,
                left: Int,
                top: Int,
                right: Int,
                bottom: Int,
                oldLeft: Int,
                oldTop: Int,
                oldRight: Int,
                oldBottom: Int
            ) {
                rvPhotoList.removeOnLayoutChangeListener(this)
                val layoutManager = rvPhotoList.layoutManager
                val viewAtPosition = layoutManager?.findViewByPosition(exitPosition)
                // Scroll to position if the view for the current position is null (not currently part of
                // layout manager children), or it's not completely visible.
                if (viewAtPosition == null || layoutManager
                        .isViewPartiallyVisible(viewAtPosition, false, true)
                ) {
                    rvPhotoList.post { layoutManager?.scrollToPosition(exitPosition) }
                }
            }
        })
    }
}
