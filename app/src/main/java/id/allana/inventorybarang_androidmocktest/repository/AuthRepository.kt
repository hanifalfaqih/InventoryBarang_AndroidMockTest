package id.allana.inventorybarang_androidmocktest.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import id.allana.inventorybarang_androidmocktest.repository.base.BaseAuthRepository
import id.allana.inventorybarang_androidmocktest.util.Resource
import id.allana.inventorybarang_androidmocktest.util.safeCallNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepository @Inject constructor(): BaseAuthRepository {

    private val auth = Firebase.auth

    override suspend fun loginUser(email: String, password: String): Resource<AuthResult> {
        return withContext(Dispatchers.IO) {
            safeCallNetwork {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                Resource.Success(result)
            }
        }
    }

}