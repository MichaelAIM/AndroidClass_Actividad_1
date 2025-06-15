package com.example.actividad1.GeoPhotoLogger

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.actividad1.GeoPhotoLogger.LocationRecord
import com.example.actividad1.GeoPhotoLogger.PhotoRecord
import com.example.actividad1.GeoPhotoLogger.LocationPhotoRepository
import kotlinx.coroutines.launch

class LocationPhotoViewModel(private val repository: LocationPhotoRepository) : ViewModel() {

    // LiveData para la ubicación actual
    private val _currentLocation = MutableLiveData<Location?>()
    val currentLocation: LiveData<Location?> = _currentLocation

    // LiveData para la última ubicación guardada (para vincular fotos)
    private val _lastSavedLocationRecordId = MutableLiveData<String?>()
    val lastSavedLocationRecordId: LiveData<String?> = _lastSavedLocationRecordId

    // LiveData para todos los registros combinados (ubicación + foto)
    val allCombinedRecords = repository.allCombinedRecords.asLiveData()

    fun updateCurrentLocation(location: Location) {
        _currentLocation.value = location
    }

    fun saveLocation() {
        currentLocation.value?.let { loc ->
            val newLocationRecord = LocationRecord(latitude = loc.latitude, longitude = loc.longitude)
            viewModelScope.launch {
                repository.insertLocationRecord(newLocationRecord)
                _lastSavedLocationRecordId.value = newLocationRecord.id // Actualiza el ID de la última ubicación guardada
            }
        }
    }

    fun savePhoto(photoPath: String) {
        // Vincula la foto a la última ubicación guardada
        lastSavedLocationRecordId.value?.let { locationId ->
            val newPhotoRecord = PhotoRecord(locationRecordId = locationId, photoPath = photoPath)
            viewModelScope.launch {
                repository.insertPhotoRecord(newPhotoRecord)
            }
        } ?: run {
            // Si no hay una ubicación guardada, puedes decidir qué hacer:
            // 1. Guardar la foto sin vincular a una ubicación (requeriría cambiar el esquema de PhotoRecord)
            // 2. Mostrar un mensaje al usuario para que guarde la ubicación primero
            // Para este ejemplo, simplemente no la guardamos si no hay ubicación vinculada.
            // Puedes añadir un Toast o un log aquí.
        }
    }
}

// Factoría para instanciar el ViewModel
class LocationPhotoViewModelFactory(private val repository: LocationPhotoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocationPhotoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LocationPhotoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}