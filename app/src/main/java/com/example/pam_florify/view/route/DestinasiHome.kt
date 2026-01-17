package com.example.pam_florify.view.route

import com.example.pam_florify.R

object DestinasiHome {
    const val route = "home"
    const val routeWithArgs = "home/{userId}/{username}"
    fun createRoute(userId: Int, username: String) = "home/$userId/$username"
}