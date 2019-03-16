package com.nicoduarte.gallery.api

import android.content.Context
import com.nicoduarte.gallery.R
import retrofit2.HttpException
import java.net.SocketTimeoutException

class ApiErrorParser (val context: Context) {

    fun parseError(error: Throwable): String? {
        return when (error) {
            is HttpException -> error.response().errorBody()?.string()
            is SocketTimeoutException -> context.getString(R.string.timeout)
            else -> context.getString(R.string.unknown_error)
        }
    }
}