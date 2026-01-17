package com.example.pam_florify.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pam_florify.modeldata.*
import com.example.pam_florify.repositori.RepositoryTanaman
import kotlinx.coroutines.launch

class EditTanamanViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositoryTanaman: RepositoryTanaman
) : ViewModel() {

    var uiStateTanaman by mutableStateOf(UIStateTanaman())
        private set

    var snackbarMessage by mutableStateOf<String?>(null)
        private set

    private val tanamanId: Int?

    init {
        tanamanId = try {
            checkNotNull(savedStateHandle["tanamanId"])
        } catch (e: IllegalStateException) {
            snackbarMessage = "Gagal memuat data tanaman: ID tidak valid."
            null
        }

        if (tanamanId != null) {
            viewModelScope.launch {
                try {
                    val tanaman = repositoryTanaman.getTanamanById(tanamanId)
                    tanaman?.let {
                        uiStateTanaman = it.toUIStateTanaman(isEntryValid = true)
                    } ?: run {
                        snackbarMessage = "Gagal memuat data tanaman: ID tidak ditemukan."
                    }
                } catch (e: Exception) {
                    snackbarMessage = "Gagal memuat data tanaman: ${e.message}"
                }
            }
        }
    }

    fun updateUiState(detailTanaman: DetailTanaman) {
        uiStateTanaman = uiStateTanaman.copy(
            detailTanaman = detailTanaman,
            isEntryValid = detailTanaman.validasi()
        )
    }

    fun resetSnackbarMessage() { snackbarMessage = null }

    fun updateTanaman() {
        if (tanamanId != null){
            viewModelScope.launch {
                try {
                    repositoryTanaman.updateTanaman(tanamanId, uiStateTanaman.detailTanaman.toTanaman())
                    snackbarMessage = "Tanaman berhasil diperbarui"
                } catch (e: Exception) {
                    snackbarMessage = "Gagal memperbarui tanaman"
                }
            }
        }
    }
}