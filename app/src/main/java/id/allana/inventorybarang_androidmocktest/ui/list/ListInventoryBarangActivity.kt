package id.allana.inventorybarang_androidmocktest.ui.list

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import id.allana.inventorybarang_androidmocktest.BuildConfig
import id.allana.inventorybarang_androidmocktest.R
import id.allana.inventorybarang_androidmocktest.adapter.ListInventoryBarangAdapter
import id.allana.inventorybarang_androidmocktest.databinding.ActivityListInventoryBarangBinding
import id.allana.inventorybarang_androidmocktest.ui.AuthViewModel
import id.allana.inventorybarang_androidmocktest.ui.InventoryBarangViewModel
import id.allana.inventorybarang_androidmocktest.ui.add.AddUpdateInventoryBarangActivity
import id.allana.inventorybarang_androidmocktest.ui.login.LoginActivity
import id.allana.inventorybarang_androidmocktest.util.EventObserver
import id.allana.inventorybarang_androidmocktest.util.ext.snackbar

@AndroidEntryPoint
class ListInventoryBarangActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListInventoryBarangBinding
    private lateinit var inventoryBarangAdapter: ListInventoryBarangAdapter
    private val viewModel: InventoryBarangViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    private val currentUser = FirebaseAuth.getInstance().currentUser

    override fun onStart() {
        super.onStart()
        if (currentUser?.uid == BuildConfig.ADMIN_UID) binding.fabAdd.visibility = View.VISIBLE else binding.fabAdd.visibility = View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListInventoryBarangBinding.inflate(layoutInflater)
        setContentView(binding.root)

        subscribeToObserver()
        viewModel.getListInventoryBarang()

        inventoryBarangAdapter = ListInventoryBarangAdapter()
        setupList()

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
            adapter = inventoryBarangAdapter
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
            inventoryBarangAdapter.inventoryBarang = list
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_option, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_logout) {
            showDialogLogout()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDialogLogout() {
        MaterialAlertDialogBuilder(this)
            .apply {
                setTitle("Keluar")
                setMessage("Apa anda yakin ingin keluar?")
                    .setPositiveButton("Keluar"){ dialog, _ ->
                        logoutUser()
                        dialog.dismiss()
                    }
                    .setNegativeButton("Tidak"){ dialog, _ ->
                        dialog.dismiss()
                    }
            }.create().show()
    }

    private fun logoutUser() {
        authViewModel.logoutUser()
        navigateToLoginScreen()
    }

    private fun navigateToLoginScreen() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}