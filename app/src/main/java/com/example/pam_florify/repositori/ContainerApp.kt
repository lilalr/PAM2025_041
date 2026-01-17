package com.example.pam_florify.repositori

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.pam_florify.network.TanamanApiService
import com.example.pam_florify.view.controllNavigasi.PetaNavigasi
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType

interface AppContainer {
    val repositoryTanaman: RepositoryTanaman
    val repositoryUser: RepositoryUser
}

class ContainerApp : AppContainer {
    private val baseUrl = "http://10.0.2.2:3000/"

    private val json = Json { ignoreUnknownKeys = true }

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: TanamanApiService by lazy {
        retrofit.create(TanamanApiService::class.java)
    }

    override val repositoryTanaman: RepositoryTanaman by lazy {
        NetworkRepositoryTanaman(retrofitService)
    }

    override val repositoryUser: RepositoryUser by lazy {
        NetworkRepositoryUser(retrofitService)
    }
}

class AplikasiFlorify : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = ContainerApp()
    }
}

@Composable
fun FlorifyApp(modifier: Modifier = Modifier) {
    PetaNavigasi()
}