package id.allana.inventorybarang_androidmocktest.ui.list

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import id.allana.inventorybarang_androidmocktest.adapter.ListInventoryBarangAdapter
import id.allana.inventorybarang_androidmocktest.databinding.ActivityListInventoryBarangBinding
import id.allana.inventorybarang_androidmocktest.ui.InventoryBarangViewModel
import id.allana.inventorybarang_androidmocktest.ui.add.AddUpdateInventoryBarangActivity
import id.allana.inventorybarang_androidmocktest.util.EventObserver
import id.allana.inventorybarang_androidmocktest.util.ext.snackbar

@AndroidEntryPoint
class ListInventoryBarangActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListInventoryBarangBinding
    private lateinit var adapter: ListInventoryBarangAdapter
    private val viewModel: InventoryBarangViewModel by viewModels()

    private val currentUser = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListInventoryBarangBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (currentUser?.uid == "nKVjOocHFPcUAfWOejAk5AkgB0w1") binding.fabAdd.visibility = View.VISIBLE else binding.fabAdd.visibility = View.GONE

        viewModel.getListInventoryBarang()
        adapter = ListInventoryBarangAdapter()
        subscribeToObserver()

        val activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            when (result.resultCode) {
                100 -> {
                    snackbar("Berhasil menambahkan data")
                }
            }
        }

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddUpdateInventoryBarangActivity::class.java)
            activityResultLauncher.launch(intent)
        }
    }

    private fun setupList() {
        binding.rvInventoryBarang.apply {
            layoutManager = LinearLayoutManager(this@ListInventoryBarangActivity)
            adapter = adapter
            setHasFixedSize(true)
        }
    }

    private fun subscribeToObserver() {
        viewModel.getListInventoryBarangStatus.observe(this, EventObserver(
            onError = {
                binding.pbInventoryBarang.visibility = View.INVISIBLE
                snackbar(it)
            },
            onLoading = {
                binding.pbInventoryBarang.visibility = View.VISIBLE
            }
        ) { list ->
            binding.pbInventoryBarang.visibility = View.INVISIBLE
            adapter.inventoryBarang = list
            if (list.isNotEmpty()) {
                setupList()
            } else {
                dataEmpty()
            }
        })
    }

    private fun dataEmpty() {
        binding.rvInventoryBarang.visibility = View.GONE
        binding.tvDataEmpty.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        viewModel.getListInventoryBarang()
    }
}