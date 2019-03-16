package com.nicoduarte.gallery.utils

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class ItemOffsetDecoration(private val mItemOffset: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            if (parent.getChildAdapterPosition(view) == 0
                || parent.getChildAdapterPosition(view) == 1) {
                top = mItemOffset
            }
            right = mItemOffset
            left =  mItemOffset
            bottom = mItemOffset
        }
    }
}
