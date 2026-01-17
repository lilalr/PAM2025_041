package com.example.pam_florify.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pam_florify.modeldata.User
import com.example.pam_florify.repositori.RepositoryUser
import kotlinx.coroutines.launch

class LoginViewModel(private val repositoryUser: RepositoryUser) : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var loginStatus by mutableStateOf("")
        private set  // ✅ Pastikan hanya ViewModel yang bisa ubah status

    var loggedInUser by mutableStateOf<User?>(null)
        private set  // ✅ Pastikan hanya ViewModel yang bisa ubah user

    fun loginAction() {
        // Reset state sebelum login baru
        loggedInUser = null

        if (email.isBlank() || password.isBlank()) {
            loginStatus = "Email dan Password harus diisi"
            return
        }

        viewModelScope.launch {
            try {
                Log.d("LoginViewModel", "Attempting login for: $email")

                val loginData = mapOf("email" to email, "password" to password)
                val response = repositoryUser.login(loginData)

                if (response != null) {
                    Log.d("LoginViewModel", "Login successful: ${response.username}")

                    // ✅ Set user DULU, baru status
                    loggedInUser = response
                    loginStatus = "Login Berhasil!"

                    Log.d("LoginViewModel", "User set: ${loggedInUser?.username}, Status: $loginStatus")
                } else {
                    Log.d("LoginViewModel", "Login failed: response is null")
                    loginStatus = "Email atau Password Salah"
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Login error: ${e.message}", e)
                loginStatus = "Gagal terhubung ke server: ${e.message}"
            }
        }
    }

    fun resetStatus() {
        loginStatus = ""
        // ✅ JANGAN reset loggedInUser di sini, biarkan tetap ada sampai navigasi selesai
    }
}