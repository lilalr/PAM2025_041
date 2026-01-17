package com.example.pam_florify.view.route

import com.example.pam_florify.R
object DestinasiDetail {
    const val route = "detail_tanaman"
    const val routeWithArgs = "detail_tanaman/{tanamanId}"
    fun createRoute(tanamanId: Int) = "detail_tanaman/$tanamanId"
}
