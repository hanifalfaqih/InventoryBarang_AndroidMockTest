package id.allana.inventorybarang_androidmocktest.repository.base

import com.google.firebase.auth.AuthResult
import id.allana.inventorybarang_androidmocktest.util.Resource

interface BaseAuthRepository {

    suspend fun loginUser(email: String, password: String): Resource<AuthResult>

    fun logoutUser()

}