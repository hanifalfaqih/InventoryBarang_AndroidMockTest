package id.allana.inventorybarang_androidmocktest.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
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

    private val database = Firebase.database.getReference("InventoryBarang")

    override suspend fun getAllInventoryBarang(): Resource<List<InventoryBarang?>> {
        return withContext(Dispatchers.IO) {
            safeCallNetwork {
                val listInventoryBarang = mutableListOf<InventoryBarang?>()
                database.addValueEventListener(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (data in snapshot.children) {
                                val item = data.getValue(InventoryBarang::class.java)
                                listInventoryBarang.add(item)
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) { }

                })
                Resource.Success(listInventoryBarang)
            }
        }
    }

    override suspend fun getDetailInventoryBarang(id: String): Resource<InventoryBarang> {
        return withContext(Dispatchers.IO) {
            safeCallNetwork {
                var dataDetail = InventoryBarang()
                database.addValueEventListener(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (data in snapshot.children)
                                dataDetail = data.child(id).getValue(InventoryBarang::class.java)!!
                        }
                    }

                    override fun onCancelled(error: DatabaseError) { }

                })
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
                database.child(idInventoryBarang).setValue(addInventoryBarang).await()
                Resource.Success(Any())
            }
        }
    }


    override suspend fun updateInventoryBarang(
        updateId: String,
        updateNamaBarang: String,
        updateJumlahBarang: String,
        updatePemasok: String,
        updateInfoTambahan: String
    ): Resource<Any> {
        return withContext(Dispatchers.IO) {
            safeCallNetwork {
                val data = mapOf(
                    "id" to updateId,
                    "namaBarang" to updateNamaBarang,
                    "jumlahBarang" to updateJumlahBarang,
                    "pemasok" to updatePemasok,
                    "infoTambahan" to updateInfoTambahan
                )
                database.child(updateId).updateChildren(data)
                Resource.Success(Any())
            }
        }
    }

    override suspend fun deleteInventoryBarang(inventoryBarang: InventoryBarang): Resource<Any> {
        return withContext(Dispatchers.IO) {
            safeCallNetwork {
                database.child(inventoryBarang.tanggal).removeValue()
                Resource.Success(Any())
            }
        }
    }

}