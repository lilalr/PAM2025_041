package com.example.pam_florify.modeldata

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val userId: Int = 0,
    val username: String = "",
    val email: String = "",
    val password: String = ""
)