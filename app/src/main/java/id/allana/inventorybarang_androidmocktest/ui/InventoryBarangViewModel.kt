package id.allana.inventorybarang_androidmocktest.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.allana.inventorybarang_androidmocktest.data.model.InventoryBarang
import id.allana.inventorybarang_androidmocktest.repository.InventoryBarangRepository
import id.allana.inventorybarang_androidmocktest.util.Event
import id.allana.inventorybarang_androidmocktest.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InventoryBarangViewModel @Inject constructor(
    private val inventoryBarangRepository: InventoryBarangRepository,
    private val dispatcherFactory: CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {

    private val _getListInventoryBarangStatus = MutableLiveData<Event<Resource<List<InventoryBarang?>>>>()
    val getListInventoryBarangStatus: LiveData<Event<Resource<List<InventoryBarang?>>>> = _getListInventoryBarangStatus

    private val _getDetailInventoryBarangStatus = MutableLiveData<Event<Resource<InventoryBarang>>>()
    val getDetailInventoryBarangStatus: LiveData<Event<Resource<InventoryBarang>>> = _getDetailInventoryBarangStatus

    private val _deleteInventoryBarangStatus = MutableLiveData<Event<Resource<Any>>>()
    val deleteInventoryBarangStatus: LiveData<Event<Resource<Any>>> = _deleteInventoryBarangStatus

    private val _updateInventoryBarangStatus = MutableLiveData<Event<Resource<Any>>>()
    val updateInventoryBarangStatus: LiveData<Event<Resource<Any>>> = _updateInventoryBarangStatus

    private val _addInventoryBarangStatus = MutableLiveData<Event<Resource<Any>>>()
    val addInventoryBarangStatus: LiveData<Event<Resource<Any>>> = _addInventoryBarangStatus

    fun getListInventoryBarang() {
        _getListInventoryBarangStatus.postValue(Event(Resource.Loading()))
        viewModelScope.launch(dispatcherFactory) {
            val result = inventoryBarangRepository.getAllInventoryBarang()
            _getListInventoryBarangStatus.postValue(Event(result))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addInventoryBarang(
        namaBarang: String,
        jumlahBarang: String,
        pemasok: String,
        infoTambahan: String
    ) {
        _addInventoryBarangStatus.postValue(Event(Resource.Loading()))
        viewModelScope.launch(dispatcherFactory) {
            val result = inventoryBarangRepository.addInventoryBarang(
                namaBarang, jumlahBarang, pemasok, infoTambahan
            )
            _addInventoryBarangStatus.postValue(Event(Resource.Success(result)))
        }
    }

    fun getDetailInventoryBarang(id: String) {
        _getDetailInventoryBarangStatus.postValue(Event(Resource.Loading()))
        viewModelScope.launch(dispatcherFactory) {
            val result = inventoryBarangRepository.getDetailInventoryBarang(id)
            _getDetailInventoryBarangStatus.postValue(Event(result))
        }
    }

    fun updateInventoryBarang(
        updateId: String,
        updateNamaBarang: String,
        updateJumlahBarang: String,
        updatePemasok: String,
        updateInfoTambahan: String
    ) {
        _updateInventoryBarangStatus.postValue(Event(Resource.Loading()))
        viewModelScope.launch(dispatcherFactory) {
            val result = inventoryBarangRepository.updateInventoryBarang(
                updateId,
                updateNamaBarang,
                updateJumlahBarang,
                updatePemasok,
                updateInfoTambahan
            )
            _updateInventoryBarangStatus.postValue(Event(Resource.Success(result)))
        }
    }

    fun deleteInventoryBarang(inventoryBarang: InventoryBarang) {
        _deleteInventoryBarangStatus.postValue(Event(Resource.Loading()))
        viewModelScope.launch(dispatcherFactory) {
            val result = inventoryBarangRepository.deleteInventoryBarang(
                inventoryBarang
            )
            _deleteInventoryBarangStatus.postValue(Event(Resource.Success(result)))
        }
    }


}