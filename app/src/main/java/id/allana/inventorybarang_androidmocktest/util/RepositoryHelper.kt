package id.allana.inventorybarang_androidmocktest.util

import com.google.firebase.database.ValueEventListener

inline fun <T> safeCallNetwork(action: () -> Resource<T>): Resource<T> {
    return try {
        action()
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Terjadi masalah tak diketahui")
    }
}