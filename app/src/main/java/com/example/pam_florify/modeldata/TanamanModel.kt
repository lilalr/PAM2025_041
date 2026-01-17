package com.example.pam_florify.modeldata

import kotlinx.serialization.Serializable

@Serializable
data class Tanaman(
    val tanamanId: Int = 0,
    val nama: String = "",
    val deskripsi: String = "",
    val kategoriId: Int = 0,
    val kategoriNama: String = "",
    val foto: String? = null,
    val userId: Int = 0
)

data class DetailTanaman(
    val tanamanId: Int = 0,
    val nama: String = "",
    val deskripsi: String = "",
    val kategoriId: Int = 0,
    val foto: String = ""
)

data class UIStateTanaman(
    val detailTanaman: DetailTanaman = DetailTanaman(),
    val isEntryValid: Boolean = false
)

data class UIStateDetailTanaman(
    val detailTanaman: DetailTanaman = DetailTanaman(),
    val listAktivitas: List<DetailAktivitas> = listOf()
)

fun Tanaman.toDetailTanaman(): DetailTanaman = DetailTanaman(
    tanamanId,
    nama,
    deskripsi,
    kategoriId,
    foto ?: ""
)

fun DetailTanaman.toTanaman(): Tanaman = Tanaman(
    tanamanId,
    nama,
    deskripsi,
    kategoriId,
    "",
    foto
)

fun Tanaman.toUIStateTanaman(isEntryValid: Boolean = false): UIStateTanaman = UIStateTanaman(
    detailTanaman = this.toDetailTanaman(),
    isEntryValid = isEntryValid
)

fun DetailTanaman.validasi(): Boolean = nama.isNotBlank() && deskripsi.isNotBlank()