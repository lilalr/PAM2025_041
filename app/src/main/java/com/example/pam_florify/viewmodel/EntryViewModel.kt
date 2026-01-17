package com.example.pam_florify.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pam_florify.modeldata.DetailTanaman
import com.example.pam_florify.modeldata.UIStateTanaman
import com.example.pam_florify.modeldata.toTanaman
import com.example.pam_florify.modeldata.validasi
import com.example.pam_florify.repositori.RepositoryTanaman
import kotlinx.coroutines.launch

class EntryViewModel(private val repositoryTanaman: RepositoryTanaman) : ViewModel() {

    var uiStateTanaman by mutableStateOf(UIStateTanaman())
        private set

    var isSaveCompleted by mutableStateOf(false)
        private set

    fun updateUiState(detailTanaman: DetailTanaman) {
        uiStateTanaman = UIStateTanaman(
            detailTanaman = detailTanaman,
            isEntryValid = detailTanaman.validasi()
        )
    }

    fun onSaveCompletedHandled() {
        isSaveCompleted = false
    }

    fun addTanaman(userId: Int) {
        viewModelScope.launch {
            try {
                val tanaman = uiStateTanaman.detailTanaman.toTanaman().copy(userId = userId)

                repositoryTanaman.insertTanaman(tanaman)
                isSaveCompleted = true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}