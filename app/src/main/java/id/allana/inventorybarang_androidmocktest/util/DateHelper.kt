package id.allana.inventorybarang_androidmocktest.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


object DateHelper {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDate(): String {
        val currentDateTime = LocalDateTime.now()
        return currentDateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
    }
}