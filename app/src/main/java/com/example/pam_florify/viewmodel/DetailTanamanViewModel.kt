package com.example.pam_florify.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.*
import com.example.pam_florify.modeldata.*
import com.example.pam_florify.repositori.RepositoryTanaman
import kotlinx.coroutines.launch

class DetailTanamanViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositoryTanaman: RepositoryTanaman
) : ViewModel() {

    private val _tanamanId: Int = savedStateHandle["tanamanId"] ?: 0

    var uiStateDetailTanaman by mutableStateOf(UIStateDetailTanaman())
        private set

    var uiStateAktivitas by mutableStateOf(UIStateAktivitas())
        private set

    var snackbarMessage by mutableStateOf<String?>(null)
        private set

    var showFormAktivitas by mutableStateOf(false)
    var isEditMode by mutableStateOf(false)

    init { getDetailTanaman() }

    fun getDetailTanaman() {
        if (_tanamanId != 0) {
            viewModelScope.launch {
                try {
                    val tanaman = repositoryTanaman.getTanamanById(_tanamanId)
                    val aktivitas = repositoryTanaman.getAktivitasByTanaman(_tanamanId)
                    uiStateDetailTanaman = UIStateDetailTanaman(
                        detailTanaman = tanaman.toDetailTanaman(),
                        listAktivitas = aktivitas.map { it.toDetailAktivitas() }
                    )
                } catch (e: retrofit2.HttpException) {
                    val errorBody = e.response()?.errorBody()?.string()
                    snackbarMessage = errorBody ?: "Gagal memuat: ${e.message()}"
                } catch (e: Exception) {
                    snackbarMessage = "Gagal memuat detail: ${e.message}"
                }
            }
        }
    }

    fun resetSnackbarMessage() { snackbarMessage = null }

    fun toggleFormAktivitas(detailAktivitas: DetailAktivitas? = null) {
        if (detailAktivitas != null) {
            uiStateAktivitas = UIStateAktivitas(detailAktivitas = detailAktivitas, isEntryValid = true)
            isEditMode = true
        } else {
            uiStateAktivitas = UIStateAktivitas()
            isEditMode = false
        }
        showFormAktivitas = !showFormAktivitas
    }

    fun updateAktivitasUiState(detailAktivitas: DetailAktivitas) {
        uiStateAktivitas = UIStateAktivitas(
            detailAktivitas = detailAktivitas,
            isEntryValid = detailAktivitas.validasi()
        )
    }

    fun simpanAktivitas() {
        viewModelScope.launch {
            try {
                val aktivitas = uiStateAktivitas.detailAktivitas.toAktivitas(_tanamanId)
                if (isEditMode) {
                    repositoryTanaman.updateAktivitas(aktivitas.aktivitasId, aktivitas)
                } else {
                    repositoryTanaman.insertAktivitas(aktivitas)
                }
                getDetailTanaman()
                showFormAktivitas = false
                snackbarMessage = "Aktivitas berhasil disimpan"
            } catch (e: Exception) {
                snackbarMessage = "Gagal menyimpan aktivitas"
            }
        }
    }

    fun hapusTanaman() {
        viewModelScope.launch {
            try {
                repositoryTanaman.deleteTanaman(_tanamanId)
                snackbarMessage = "Tanaman berhasil dihapus"
                // Setelah hapus, data akan kosong, jadi tidak perlu refresh
            } catch (e: Exception) {
                snackbarMessage = "Gagal menghapus tanaman: ${e.message}"
            }
        }
    }

    fun hapusAktivitas(aktivitasId: Int) {
        viewModelScope.launch {
            try {
                repositoryTanaman.deleteAktivitas(aktivitasId)
                getDetailTanaman() // Refresh list aktivitas
                snackbarMessage = "Aktivitas berhasil dihapus"
            } catch (e: Exception) {
                snackbarMessage = "Gagal menghapus aktivitas: ${e.message}"
            }
        }
    }
}