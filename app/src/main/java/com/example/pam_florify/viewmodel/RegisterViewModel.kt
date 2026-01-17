package com.example.pam_florify.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pam_florify.modeldata.User
import com.example.pam_florify.repositori.RepositoryUser
import kotlinx.coroutines.launch

class RegisterViewModel(private val repositoryUser: RepositoryUser) : ViewModel() {
    var username by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")

    var snackbarMessage by mutableStateOf<String?>(null)
        private set

    fun resetSnackbarMessage() {
        snackbarMessage = null
    }

    fun register() {
        if (username.isBlank() || email.isBlank() || password.isBlank()) {
            snackbarMessage = "Semua kolom harus diisi!"
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            snackbarMessage = "Format email salah!"
            return
        }

        if (password.length < 6) {
            snackbarMessage = "Password minimal 6 karakter!"
            return
        }

        viewModelScope.launch {
            try {
                val response = repositoryUser.register(
                    User(username = username, email = email, password = password)
                )

                if (response.isSuccessful) {
                    // SET PESAN SAJA, navigasi akan dipicu oleh LaunchedEffect di UI
                    snackbarMessage = "Registrasi Berhasil!"
                } else {
                    val errorBody = response.errorBody()?.string()
                    if (errorBody?.contains("email", ignoreCase = true) == true) {
                        snackbarMessage = "Email sudah digunakan, gunakan email lain!"
                    } else {
                        snackbarMessage = "Registrasi Gagal: ${response.message()}"
                    }
                }
            } catch (e: Exception) {
                snackbarMessage = "Terjadi kesalahan jaringan: ${e.message}"
            }
        }
    }
}