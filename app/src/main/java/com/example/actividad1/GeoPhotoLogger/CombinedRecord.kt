package com.example.actividad1.GeoPhotoLogger

import android.net.Uri

data class CombinedRecord(
    val locationRecordId: String,
    val latitude: Double,
    val longitude: Double,
    val locationTimestamp: Long,
    val photoRecordId: String?, // Puede ser nulo si no hay foto asociada
    val photoPath: String?, // Puede ser nulo
    val photoTimestamp: Long? // Puede ser nulo
) {
    // Helper para obtener el URI de la foto
    fun getPhotoUri(): Uri? {
        return photoPath?.let { Uri.parse(it) }
    }
}