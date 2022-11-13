package id.allana.inventorybarang_androidmocktest.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import id.allana.inventorybarang_androidmocktest.repository.AuthRepository
import id.allana.inventorybarang_androidmocktest.util.Event
import id.allana.inventorybarang_androidmocktest.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val dispatcherFactory: CoroutineDispatcher = Dispatchers.Main
): ViewModel() {

    private val _loginStatusUser = MutableLiveData<Event<Resource<AuthResult>>>()
    val loginStatusUser: LiveData<Event<Resource<AuthResult>>> = _loginStatusUser

    fun loginUser(email: String, password: String) {
        _loginStatusUser.postValue(Event(Resource.Loading()))
        viewModelScope.launch(dispatcherFactory) {
            val result = authRepository.loginUser(email, password)
            _loginStatusUser.postValue(Event(result))
        }
    }

    fun logoutUser() {
        authRepository.logoutUser()
    }

}