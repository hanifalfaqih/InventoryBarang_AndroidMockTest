package id.allana.inventorybarang_androidmocktest.util.ext

import android.app.Activity
import com.google.android.material.snackbar.Snackbar

fun Activity.snackbar(message: String) {
    Snackbar.make(window.decorView.rootView, message, Snackbar.LENGTH_LONG).show()
}