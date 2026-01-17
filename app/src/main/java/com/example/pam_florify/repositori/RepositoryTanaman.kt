package com.example.pam_florify.repositori

import com.example.pam_florify.modeldata.Aktivitas
import com.example.pam_florify.modeldata.Tanaman
import com.example.pam_florify.network.TanamanApiService
import okhttp3.ResponseBody
import retrofit2.Response

interface RepositoryTanaman {
    suspend fun getTanaman(
        userId: Int): List<Tanaman>
    suspend fun insertTanaman(
        tanaman: Tanaman): Response<ResponseBody>
    suspend fun updateTanaman(
        id: Int, tanaman: Tanaman): Response<ResponseBody>
    suspend fun deleteTanaman(
        id: Int): Response<ResponseBody>
    suspend fun getTanamanById(
        id: Int): Tanaman
    suspend fun getAktivitasByTanaman(id: Int): List<Aktivitas>
    suspend fun insertAktivitas(
        aktivitas: Aktivitas): Response<ResponseBody>
    suspend fun deleteAktivitas(
        id: Int): Response<ResponseBody>
    suspend fun updateAktivitas(
        id: Int, aktivitas: Aktivitas): Response<ResponseBody>
}

class NetworkRepositoryTanaman(private val tanamanApiService: TanamanApiService) : RepositoryTanaman {
    override suspend fun getTanaman(
        userId: Int): List<Tanaman> = tanamanApiService.getTanaman(userId)
    override suspend fun insertTanaman(
        tanaman: Tanaman) = tanamanApiService.insertTanaman(tanaman)
    override suspend fun updateTanaman(
        id: Int, tanaman: Tanaman) = tanamanApiService.updateTanaman(id, tanaman)
    override suspend fun deleteTanaman(
        id: Int) = tanamanApiService.deleteTanaman(id)
    override suspend fun getTanamanById(
        id: Int): Tanaman = tanamanApiService.getTanamanById(id)
    override suspend fun getAktivitasByTanaman(
        id: Int): List<Aktivitas> = tanamanApiService.getAktivitasByTanaman(id)
    override suspend fun insertAktivitas(
        aktivitas: Aktivitas) = tanamanApiService.insertAktivitas(aktivitas)
    override suspend fun deleteAktivitas(
        id: Int) = tanamanApiService.deleteAktivitas(id)
    override suspend fun updateAktivitas(
        id: Int, aktivitas: Aktivitas) = tanamanApiService.updateAktivitas(id, aktivitas)
}