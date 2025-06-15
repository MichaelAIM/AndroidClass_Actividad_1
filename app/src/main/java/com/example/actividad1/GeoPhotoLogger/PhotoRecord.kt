package com.example.actividad1.GeoPhotoLogger


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index // <-- ¡Asegúrate de tener esta importación!
import java.util.UUID

@Entity(
    tableName = "photo_records",
    foreignKeys = [ForeignKey(
        entity = LocationRecord::class,
        parentColumns = ["id"],
        childColumns = ["locationRecordId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["locationRecordId"])] // <-- ¡Añade esta línea!
)
data class PhotoRecord(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val locationRecordId: String,
    val photoPath: String,
    val timestamp: Long = System.currentTimeMillis()
)