package id.allana.inventorybarang_androidmocktest.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import id.allana.inventorybarang_androidmocktest.R
import id.allana.inventorybarang_androidmocktest.databinding.ActivityLoginBinding
import id.allana.inventorybarang_androidmocktest.ui.AuthViewModel
import id.allana.inventorybarang_androidmocktest.ui.list.ListInventoryBarangActivity
import id.allana.inventorybarang_androidmocktest.util.EventObserver
import id.allana.inventorybarang_androidmocktest.util.FieldValidators
import id.allana.inventorybarang_androidmocktest.util.ext.snackbar

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        if (FirebaseAuth.getInstance().currentUser != null) {
            Intent(this, ListInventoryBarangActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }

        subscribeToStateObserver()

        binding.btnLogin.setOnClickListener {
            setupTextFieldValidation()
            if (isValidate()) {
                viewModel.loginUser(
                    binding.etEmail.text.toString(),
                    binding.etPassword.text.toString()
                )
            }
        }
    }

    private fun subscribeToStateObserver() {
        viewModel.loginStatusUser.observe(this, EventObserver(
            onError = {
                binding.progressBarLogin.visibility = View.INVISIBLE
                snackbar(it)
            },
            onLoading = { binding.progressBarLogin.visibility = View.VISIBLE }
        ) {
            binding.progressBarLogin.visibility = View.INVISIBLE
            Intent(this, ListInventoryBarangActivity::class.java).also {
                startActivity(it)
                finish()
            }
        })
    }

    private fun validateEmail(): Boolean {
        if (!FieldValidators.isFormatEmailValid(binding.etEmail.text.toString())) {
            binding.tilEmail.error = "Format email salah"
            return false
        } else {
            binding.tilEmail.error = null
        }
        return true
    }

    private fun validatePassword(): Boolean {
        if (binding.etPassword.text.toString().isEmpty()) {
            binding.tilPassword.error = "Password tidak boleh kosong"
            return false
        } else {
            binding.tilPassword.error = null
        }
        return true
    }

    private fun isValidate() = validateEmail() && validatePassword()

    private fun setupTextFieldValidation() {
        binding.etEmail.addTextChangedListener(TextFieldValidation(binding.etEmail))
        binding.etPassword.addTextChangedListener(TextFieldValidation(binding.etPassword))
    }

    inner class TextFieldValidation(private val view: View): TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            when (view.id) {
                R.id.et_email -> { validateEmail() }
                R.id.et_password -> { validatePassword() }
            }
        }
        override fun afterTextChanged(p0: Editable?) {}
    }


}