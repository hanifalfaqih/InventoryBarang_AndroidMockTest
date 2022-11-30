package id.allana.inventorybarang_androidmocktest.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import id.allana.inventorybarang_androidmocktest.data.model.InventoryBarang
import id.allana.inventorybarang_androidmocktest.repository.base.BaseInventoryBarangRepository
import id.allana.inventorybarang_androidmocktest.util.DateHelper
import id.allana.inventorybarang_androidmocktest.util.Resource
import id.allana.inventorybarang_androidmocktest.util.safeCallNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class InventoryBarangRepository @Inject constructor(): BaseInventoryBarangRepository {

    private val data = Firebase.firestore.collection("InventoryBarang")

    override suspend fun getAllInventoryBarang(): Resource<List<InventoryBarang?>> {
        return withContext(Dispatchers.IO) {
            safeCallNetwork {
                val listInventoryBarang = data.orderBy("tanggal", Query.Direction.DESCENDING)
                    .get()
                    .await()
                    .toObjects(InventoryBarang::class.java)
                Resource.Success(listInventoryBarang)
            }
        }
    }

    override suspend fun getDetailInventoryBarang(id: String): Resource<InventoryBarang?> {
        return withContext(Dispatchers.IO) {
            safeCallNetwork {
                val dataDetail = data
                    .document(id)
                    .get()
                    .await()
                    .toObject(InventoryBarang::class.java)
                Resource.Success(dataDetail)
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun addInventoryBarang(
        namaBarang: String,
        jumlahBarang: String,
        pemasok: String,
        infoTambahan: String
    ): Resource<Any> {
        return withContext(Dispatchers.IO) {
            safeCallNetwork {
                val idInventoryBarang = UUID.randomUUID().toString()
                val tanggal = DateHelper.getCurrentDate()
                val addInventoryBarang = InventoryBarang(
                    id = idInventoryBarang,
                    namaBarang = namaBarang,
                    jumlahBarang = jumlahBarang,
                    pemasok = pemasok,
                    tanggal = tanggal,
                    infoTambahan = infoTambahan
                )
                data.document(idInventoryBarang).set(addInventoryBarang).await()
                Resource.Success(Any())
            }
        }
    }

    override suspend fun updateInventoryBarang(
        oldData: InventoryBarang,
        newData: Map<String, Any>
    ): Resource<Any> {
        return withContext(Dispatchers.IO) {
            safeCallNetwork {
                val dataQuery = data
                    .whereEqualTo("id", oldData.id)
                    .get()
                    .await()
                if (dataQuery.documents.isNotEmpty()) {
                    for (document in dataQuery) {
                        try {
                            data.document(oldData.id).set(
                                newData,
                                SetOptions.merge()
                            ).await()
                        } catch (e: Exception) {
                            Resource.Error(e.toString(), null)
                        }
                    }
                }
                Resource.Success(Any())
            }
        }
    }

    override suspend fun deleteInventoryBarang(inventoryBarang: InventoryBarang): Resource<Any> {
        return withContext(Dispatchers.IO) {
            safeCallNetwork {
                data.document(inventoryBarang.id).delete().await()
                Resource.Success(inventoryBarang)
            }
        }
    }

}