package id.allana.inventorybarang_androidmocktest.ui.add

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import id.allana.inventorybarang_androidmocktest.data.model.InventoryBarang
import id.allana.inventorybarang_androidmocktest.databinding.ActivityAddUpdateInventoryBarangBinding
import id.allana.inventorybarang_androidmocktest.ui.InventoryBarangViewModel
import id.allana.inventorybarang_androidmocktest.util.DateHelper
import id.allana.inventorybarang_androidmocktest.util.EventObserver
import id.allana.inventorybarang_androidmocktest.util.ext.snackbar

@AndroidEntryPoint
class AddUpdateInventoryBarangActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_INVENTORY_BARANG = "extra_inventory_barang"
        const val RESULT_ADD = 100
        const val RESULT_UPDATE = 200
    }

    private lateinit var binding: ActivityAddUpdateInventoryBarangBinding
    private val viewModel: InventoryBarangViewModel by viewModels()
    private var data: InventoryBarang? = null
    private var isEdit = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUpdateInventoryBarangBinding.inflate(layoutInflater)
        setContentView(binding.root)

        subscribeObserverAdd()
        subscribeObserverUpdate()
        handleOnBackPressedCallback()

        data = intent.getParcelableExtra(EXTRA_INVENTORY_BARANG)
        if (data != null) isEdit = true else data = InventoryBarang()

        val actionBarTitle: String
        val btnTitle: String

        if (isEdit) {
            actionBarTitle = "Perbarui"
            btnTitle = "Perbarui"
            data?.let {
                binding.etNamaBarang.setText(it.namaBarang)
                binding.etJumlahBarang.setText(it.jumlahBarang)
                binding.etNamaPemasok.setText(it.pemasok)
                binding.etInfoTambahan.setText(it.infoTambahan)
            }
        } else {
            actionBarTitle = "Tambah"
            btnTitle = "Tambah"
        }

        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnAddUpdate.text = btnTitle

        binding.btnAddUpdate.setOnClickListener {
            handleAddUpdate()
        }
    }

    private fun handleOnBackPressedCallback() {
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showAlertDialog()
            }
        }
        this.onBackPressedDispatcher.addCallback(
            this, callback
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleAddUpdate() {
        val namaBarang = binding.etNamaBarang.text.toString().trim()
        val jumlahBarang = binding.etJumlahBarang.text.toString().trim()
        val pemasok = binding.etNamaPemasok.text.toString().trim()
        val infoTambahan = binding.etInfoTambahan.text.toString().trim()

        val map = mutableMapOf<String, Any>()
        map["namaBarang"] = namaBarang
        map["jumlahBarang"] = jumlahBarang
        map["pemasok"] = pemasok
        map["infoTambahan"] = infoTambahan
        map["tanggal"] = DateHelper.getCurrentDate()

        if (isEdit) {
            viewModel.updateInventoryBarang(data!!, map)
            val intent = Intent()
            intent.putExtra(EXTRA_INVENTORY_BARANG, data)
            setResult(RESULT_UPDATE)
        } else {
            viewModel.addInventoryBarang(namaBarang, jumlahBarang, pemasok, infoTambahan)
            setResult(RESULT_ADD)
        }
    }

    private fun subscribeObserverAdd() {
        viewModel.addInventoryBarangStatus.observe(this, EventObserver(
            onError = {
                binding.pbInventoryBarang.visibility = View.INVISIBLE
                snackbar(it)
            },
            onLoading = {
                binding.pbInventoryBarang.visibility = View.VISIBLE
            }
        ) {
            binding.pbInventoryBarang.visibility = View.INVISIBLE
            finish()
        })
    }

    private fun subscribeObserverUpdate() {
        viewModel.updateInventoryBarangStatus.observe(this, EventObserver(
            onError = {
                binding.pbInventoryBarang.visibility = View.INVISIBLE
                snackbar(it)
            },
            onLoading = {
                binding.pbInventoryBarang.visibility = View.VISIBLE
            }
        ) {
            binding.pbInventoryBarang.visibility - View.INVISIBLE
            finish()
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> showAlertDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAlertDialog() {
        val dialogTitle = "Batal"
        val dialogMessage =
            "Apa kamu yakin akan membatalkan tanpa menyimpan? Semua yang sudah ditulis dalam form akan dihapus"
        MaterialAlertDialogBuilder(this).apply {
            setTitle(dialogTitle)
            setMessage(dialogMessage)
            setCancelable(false)
            setPositiveButton("Batal") { _, _ ->
                finish()
            }
            setNegativeButton("Tidak") { dialog, _ -> dialog.cancel() }
        }.create().show()
    }
}