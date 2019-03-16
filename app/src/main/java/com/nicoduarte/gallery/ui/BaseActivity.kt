package com.nicoduarte.gallery.ui

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.moviesdem.ui.IToolbar

open class BaseActivity : AppCompatActivity(), IToolbar {

    protected var _toolbar: Toolbar? = null

    override fun toolbarToLoad(toolbar: Toolbar?) {
        _toolbar = toolbar
        _toolbar?.let { setSupportActionBar(_toolbar) }
    }

    override fun enableHomeDisplay(value: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(value)
    }
}
