package id.allana.inventorybarang_androidmocktest.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import id.allana.inventorybarang_androidmocktest.data.model.InventoryBarang
import id.allana.inventorybarang_androidmocktest.databinding.ActivityDetailInventoryBarangBinding
import id.allana.inventorybarang_androidmocktest.ui.InventoryBarangViewModel
import id.allana.inventorybarang_androidmocktest.ui.add.AddUpdateInventoryBarangActivity
import id.allana.inventorybarang_androidmocktest.util.EventObserver
import id.allana.inventorybarang_androidmocktest.util.ext.snackbar

@AndroidEntryPoint
class DetailInventoryBarangActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_INVENTORY_BARANG = "extra_inventory_barang"
    }

    private lateinit var binding: ActivityDetailInventoryBarangBinding
    private lateinit var id: String
    private var data: InventoryBarang? = null
    private val viewModel: InventoryBarangViewModel by viewModels()
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailInventoryBarangBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Detail"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        data = intent.getParcelableExtra(EXTRA_INVENTORY_BARANG)
        id = data?.id.toString()

        viewModel.getDetailInventoryBarang(id)

        subscribeToObserveDetail()
        subscribeToObserverDelete()
        handleOnBackPressedCallback()

        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            when (result.resultCode) {
                200 -> {
                    snackbar("Berhasil memperbarui data")
                }
            }
        }

        binding.btnEdit.setOnClickListener { handleEdit() }
        binding.btnDelete.setOnClickListener { handleDelete() }
    }

    private fun handleOnBackPressedCallback() {
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() { finish() }
        }
        this.onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }

    private fun handleEdit() {
        val intent = Intent(this, AddUpdateInventoryBarangActivity::class.java)
        intent.putExtra(AddUpdateInventoryBarangActivity.EXTRA_INVENTORY_BARANG, data)
        activityResultLauncher.launch(intent)
    }

    private fun handleDelete() {
        showAlertDialog()
    }

    private fun subscribeToObserveDetail() {
        viewModel.getDetailInventoryBarangStatus.observe(this, EventObserver(
            onError = {
                binding.pbDetail.visibility = View.INVISIBLE
                snackbar(it)
            },
            onLoading = {
                binding.pbDetail.visibility = View.VISIBLE
            }
        ) { data ->
            binding.pbDetail.visibility = View.INVISIBLE
            binding.tvNamaBarang.text = data.namaBarang
            binding.tvBanyakBarang.text = data.jumlahBarang
            binding.tvNamaPemasok.text = data.pemasok
            binding.tvTanggal.text = data.tanggal
            binding.tvInfoTambahan.text = data.infoTambahan

        })
    }

    private fun subscribeToObserverDelete() {
        viewModel.deleteInventoryBarangStatus.observe(this, EventObserver(
            onError = {
                binding.pbDetail.visibility = View.INVISIBLE
                snackbar(it)
            },
            onLoading = {
                binding.pbDetail.visibility = View.VISIBLE
            }
        ) {
            binding.pbDetail.visibility = View.INVISIBLE
            finish()
        })
    }

    private fun showAlertDialog() {
        val dialogTitle = "Hapus"
        val dialogMessage = "Apakah anda yakin ingin menghapus data ini?"
        val alertDialogBuilder = AlertDialog.Builder(this)
        with(alertDialogBuilder) {
            setTitle(dialogTitle)
            setMessage(dialogMessage)
            setCancelable(false)
            setPositiveButton("Hapus") { _, _ ->
                data?.let {
                    viewModel.deleteInventoryBarang(it)
                }
                finish()
            }
            setNegativeButton("Tidak") { dialog, _ -> dialog.cancel() }
        }
        alertDialogBuilder.create().show()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getDetailInventoryBarang(id)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

}