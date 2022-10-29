package id.allana.inventorybarang_androidmocktest.repository.base

import id.allana.inventorybarang_androidmocktest.data.model.InventoryBarang
import id.allana.inventorybarang_androidmocktest.util.Resource

interface BaseInventoryBarangRepository {

    suspend fun getAllInventoryBarang(): Resource<List<InventoryBarang?>>

    suspend fun addInventoryBarang(
        namaBarang: String,
        jumlahBarang: String,
        pemasok: String,
        infoTambahan: String
    ): Resource<Any>

    suspend fun updateInventoryBarang(
        updateId: String,
        updateNamaBarang: String,
        updateJumlahBarang: String,
        updatePemasok: String,
        updateInfoTambahan: String
    ): Resource<Any>

    suspend fun deleteInventoryBarang(inventoryBarang: InventoryBarang): Resource<Any>

    suspend fun getDetailInventoryBarang(id: String): Resource<InventoryBarang>

}