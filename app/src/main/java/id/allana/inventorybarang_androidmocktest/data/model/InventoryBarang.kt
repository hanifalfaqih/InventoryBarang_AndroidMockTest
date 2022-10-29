package id.allana.inventorybarang_androidmocktest.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InventoryBarang(
    val id: String = "",
    val namaBarang: String = "",
    val jumlahBarang: String = "",
    val pemasok: String = "",
    val tanggal: String = "",
    val infoTambahan: String = ""
): Parcelable