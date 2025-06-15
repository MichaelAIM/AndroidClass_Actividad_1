package com.example.actividad1.GeoPhotoLogger

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

// Define la tabla para los registros de ubicación
@Entity(tableName = "location_records")
data class LocationRecord(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(), // ID único para cada registro de ubicación
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long = System.currentTimeMillis() // Marca de tiempo cuando se guardó
)