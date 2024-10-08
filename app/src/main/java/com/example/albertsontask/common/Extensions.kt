@file:Suppress("DEPRECATION")

package com.example.albertsontask.common

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager


fun Activity.setLightStatusBar(isLight: Boolean) {
    val flags = if (isLight) {
        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    } else {
        0
    }
    window.decorView.systemUiVisibility = flags
}

fun Activity.hideKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = currentFocus
    view?.let {
        imm.hideSoftInputFromWindow(it.windowToken, 0)
    }
}

