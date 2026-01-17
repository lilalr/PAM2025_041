package com.example.pam_florify.view.route

import com.example.pam_florify.R

object DestinasiEdit {
    const val route = "edit_tanaman"
    const val routeWithArgs = "edit_tanaman/{tanamanId}"
    fun createRoute(tanamanId: Int) = "edit_tanaman/$tanamanId"
}