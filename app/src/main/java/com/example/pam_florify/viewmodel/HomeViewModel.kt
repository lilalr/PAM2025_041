package com.example.pam_florify.viewmodel



import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pam_florify.modeldata.Tanaman
import com.example.pam_florify.repositori.RepositoryTanaman
import kotlinx.coroutines.launch

sealed interface HomeUiState {
    data class Success(val tanaman: List<Tanaman>) : HomeUiState
    object Error : HomeUiState
    object Loading : HomeUiState
}

class HomeViewModel(private val repositoryTanaman: RepositoryTanaman) : ViewModel() {
    var homeUiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set

    var snackbarMessage: String? by mutableStateOf(null)
        private set

    fun getTanaman(userId: Int) {
        viewModelScope.launch {
            homeUiState = HomeUiState.Loading
            try {
// Sekarang ini tidak akan error lagi karena repository sudah diupdate

                val listTanaman = repositoryTanaman.getTanaman(userId)

                homeUiState = HomeUiState.Success(listTanaman)

            } catch (e: Exception) {

                homeUiState = HomeUiState.Error

            }

        }

    }



    fun getTanamanByUserId(userId: Int) {

        viewModelScope.launch {

            homeUiState = HomeUiState.Loading

            try {

// Pastikan repository kamu punya fungsi ini

                val listData = repositoryTanaman.getTanaman(userId)

                homeUiState = HomeUiState.Success(listData)

            } catch (e: Exception) {

                homeUiState = HomeUiState.Error

            }

        }

    }



    fun deleteTanaman(id: Int, userId: Int) {

        viewModelScope.launch {

            try {

                repositoryTanaman.deleteTanaman(id)

                snackbarMessage = "Berhasil menghapus tanaman"

                getTanaman(userId) // Refresh data setelah hapus

            } catch (e: Exception) {

                snackbarMessage = "Gagal menghapus: ${e.message}"

            }

        }

    }



    fun resetSnackbarMessage() { snackbarMessage = null }

}