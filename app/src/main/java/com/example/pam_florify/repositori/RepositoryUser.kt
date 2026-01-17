package com.example.pam_florify.repositori

import android.util.Log
import com.example.pam_florify.modeldata.User
import com.example.pam_florify.network.TanamanApiService
import okhttp3.ResponseBody
import retrofit2.Response

interface RepositoryUser {
    suspend fun login(
        loginData: Map<String, String>): User?
    suspend fun register(
        user: User): Response<ResponseBody>
}

class NetworkRepositoryUser(
    private val apiService: TanamanApiService
) : RepositoryUser {
    override suspend fun login(
        loginData: Map<String, String>): User? {
        Log.d("NetworkRepository", "Sending login request: $loginData")
        val response = apiService.login(loginData)
        Log.d("NetworkRepository", "Login successful - User: ${response.username}, ID: ${response.userId}")
        return response
    }

    override suspend fun register(user: User): Response<ResponseBody> {
        return apiService.register(user)
    }
}