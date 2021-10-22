package com.acadia.punk.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService

inline fun <reified R> MutableList<*>.removeIsInstance() {
    filterIsInstance<R>().reversed().forEach { remove(it) }
}

inline fun <reified T : Activity> Context.startActivity(initializer: Intent.() -> Unit = {}) {
    val intent = Intent(this, T::class.java).apply {
        initializer()
    }

    startActivity(intent)
}

fun View.hideKeyboard() {
    try {
        val imm = context.getSystemService<InputMethodManager>()
        imm?.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    } catch (ignored: Exception) {
    }
}
