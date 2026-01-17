package com.example.pam_florify.view.route

import com.example.pam_florify.R

object DestinasiEntry : DestinasiNavigasi {
    override val route = "entry_tanaman"
    override val titleRes = R.string.title_tambah_tanaman
    const val routeWithArgs = "entry_tanaman/{userId}"
    fun createRoute(userId: Int) = "entry_tanaman/$userId"
}