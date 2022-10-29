package id.allana.inventorybarang_androidmocktest.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import id.allana.inventorybarang_androidmocktest.data.model.InventoryBarang
import id.allana.inventorybarang_androidmocktest.databinding.ListInventoryBarangBinding
import id.allana.inventorybarang_androidmocktest.ui.add.AddUpdateInventoryBarangActivity
import id.allana.inventorybarang_androidmocktest.ui.detail.DetailInventoryBarangActivity

class ListInventoryBarangAdapter : RecyclerView.Adapter<ListInventoryBarangAdapter.InventoryBarangViewHolder>() {

    private val diffCallback = object: DiffUtil.ItemCallback<InventoryBarang>() {
        override fun areItemsTheSame(oldItem: InventoryBarang, newItem: InventoryBarang): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: InventoryBarang, newItem: InventoryBarang): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var inventoryBarang: List<InventoryBarang?>
    get() = differ.currentList
    set(value) = differ.submitList(value)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListInventoryBarangAdapter.InventoryBarangViewHolder {
        val listBinding = ListInventoryBarangBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InventoryBarangViewHolder(listBinding)
    }

    override fun onBindViewHolder(
        holder: ListInventoryBarangAdapter.InventoryBarangViewHolder,
        position: Int
    ) {
        val list = inventoryBarang[position]
        holder.bind(list)
    }

    override fun getItemCount(): Int = inventoryBarang.size

    inner class InventoryBarangViewHolder(private val binding: ListInventoryBarangBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(inventoryBarang: InventoryBarang?) {
            with(binding) {
                tvTanggal.text = inventoryBarang?.tanggal
                tvNamaBarang.text = inventoryBarang?.namaBarang
                tvBanyakBarang.text = inventoryBarang?.jumlahBarang.toString()
                tvNamaPemasok.text = inventoryBarang?.pemasok

                itemView.setOnClickListener {
                    val intentToDetail = Intent(itemView.context, DetailInventoryBarangActivity::class.java)
                    intentToDetail.putExtra(DetailInventoryBarangActivity.EXTRA_INVENTORY_BARANG, inventoryBarang)
                    itemView.context.startActivity(intentToDetail)
                }
            }
        }
    }
}