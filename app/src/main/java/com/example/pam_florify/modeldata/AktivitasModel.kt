package com.example.pam_florify.modeldata

import kotlinx.serialization.Serializable

@Serializable
data class Aktivitas(
    val aktivitasId: Int = 0,
    val tanamanId: Int = 0,
    val tipeAktivitas: String = "",
    val tanggalAktivitas: String = "",
    val notes: String = "",
    val createdAt: String = ""
)

data class DetailAktivitas(
    val aktivitasId: Int = 0,
    val tipeAktivitas: String = "",
    val tanggalAktivitas: String = "",
    val notes: String = ""
)

data class UIStateAktivitas(
    val detailAktivitas: DetailAktivitas = DetailAktivitas(),
    val isEntryValid: Boolean = false
)

fun DetailAktivitas.validasi(): Boolean {
    return tipeAktivitas.isNotBlank() && tanggalAktivitas.isNotBlank()
}
// Fungsi Konversi dari API ke UI
fun Aktivitas.toDetailAktivitas(): DetailAktivitas = DetailAktivitas(
    aktivitasId = aktivitasId,
    tipeAktivitas = tipeAktivitas,
    tanggalAktivitas = tanggalAktivitas,
    notes = notes
)

// Fungsi Konversi dari UI ke API
fun DetailAktivitas.toAktivitas(tanamanId: Int): Aktivitas = Aktivitas(
    aktivitasId = aktivitasId,
    tanamanId = tanamanId,
    tipeAktivitas = tipeAktivitas,
    tanggalAktivitas = tanggalAktivitas,
    notes = notes
)