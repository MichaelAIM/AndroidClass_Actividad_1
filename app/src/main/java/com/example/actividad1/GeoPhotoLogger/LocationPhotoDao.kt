package com.example.actividad1.GeoPhotoLogger


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy // <-- ¡Asegúrate de tener esta importación!
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationPhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) // <-- ¡CORRECCIÓN AQUÍ!
    suspend fun insertLocationRecord(record: LocationRecord)

    @Insert(onConflict = OnConflictStrategy.REPLACE) // <-- ¡CORRECCIÓN AQUÍ!
    suspend fun insertPhotoRecord(record: PhotoRecord)

    @Query("""
        SELECT
            lr.id AS locationRecordId,
            lr.latitude,
            lr.longitude,
            lr.timestamp AS locationTimestamp,
            pr.id AS photoRecordId,
            pr.photoPath,
            pr.timestamp AS photoTimestamp
        FROM location_records AS lr
        LEFT JOIN photo_records AS pr ON lr.id = pr.locationRecordId
        ORDER BY lr.timestamp DESC
    """)
    fun getAllCombinedRecords(): Flow<List<CombinedRecord>>

    @Query("SELECT * FROM location_records WHERE id = :id LIMIT 1")
    suspend fun getLocationRecordById(id: String): LocationRecord?

    @Query("SELECT * FROM photo_records WHERE locationRecordId = :locationRecordId LIMIT 1")
    suspend fun getPhotoRecordByLocationId(locationRecordId: String): PhotoRecord?
}