package com.example.pam_florify.network

import com.example.pam_florify.modeldata.Aktivitas
import com.example.pam_florify.modeldata.Tanaman
import com.example.pam_florify.modeldata.User
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface TanamanApiService {
    @POST("login")
    suspend fun login(@Body loginData: Map<String, String>): User

    @POST("register")
    suspend fun register(@Body user: User): Response<ResponseBody>

    @GET("tanaman")
    suspend fun getTanaman(@Query("userId") userId: Int): List<Tanaman>

    @GET("tanaman/{id}")
    suspend fun getTanamanById(@Path("id") id: Int): Tanaman

    @POST("tanaman")
    suspend fun insertTanaman(@Body tanaman: Tanaman): Response<ResponseBody>

    @PUT("tanaman/{id}")
    suspend fun updateTanaman(@Path("id") id: Int, @Body tanaman: Tanaman): Response<ResponseBody>

    @DELETE("tanaman/{id}")
    suspend fun deleteTanaman(@Path("id") id: Int): Response<ResponseBody>

    // --- AKTIVITAS ---
    @GET("tanaman/{id}/aktivitas")
    suspend fun getAktivitasByTanaman(@Path("id") id: Int): List<Aktivitas>

    @POST("aktivitas")
    suspend fun insertAktivitas(@Body aktivitas: Aktivitas): Response<ResponseBody>

    @DELETE("aktivitas/{id}")
    suspend fun deleteAktivitas(@Path("id") id: Int): Response<ResponseBody>

    @PUT("aktivitas/{id}")
    suspend fun updateAktivitas(@Path("id") id: Int, @Body aktivitas: Aktivitas): Response<ResponseBody>
}