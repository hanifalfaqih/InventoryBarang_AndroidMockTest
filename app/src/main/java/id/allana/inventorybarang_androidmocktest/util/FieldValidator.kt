package id.allana.inventorybarang_androidmocktest.util

import android.util.Patterns

object FieldValidators {
    fun isFormatEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}