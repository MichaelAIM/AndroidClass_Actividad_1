package com.example.actividad1.GeoPhotoLogger


import com.example.actividad1.GeoPhotoLogger.CombinedRecord
import com.example.actividad1.GeoPhotoLogger.LocationPhotoDao
import com.example.actividad1.GeoPhotoLogger.LocationRecord
import com.example.actividad1.GeoPhotoLogger.PhotoRecord
import kotlinx.coroutines.flow.Flow

class LocationPhotoRepository(private val locationPhotoDao: LocationPhotoDao) {

    val allCombinedRecords: Flow<List<CombinedRecord>> = locationPhotoDao.getAllCombinedRecords()

    suspend fun insertLocationRecord(record: LocationRecord) {
        locationPhotoDao.insertLocationRecord(record)
    }

    suspend fun insertPhotoRecord(record: PhotoRecord) {
        locationPhotoDao.insertPhotoRecord(record)
    }

    suspend fun getLocationRecordById(id: String): LocationRecord? {
        return locationPhotoDao.getLocationRecordById(id)
    }

    suspend fun getPhotoRecordByLocationId(locationRecordId: String): PhotoRecord? {
        return locationPhotoDao.getPhotoRecordByLocationId(locationRecordId)
    }
}