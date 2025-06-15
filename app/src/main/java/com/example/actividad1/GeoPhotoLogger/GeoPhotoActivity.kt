package com.example.actividad1.GeoPhotoLogger


import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.actividad1.R
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri

import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels

import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.actividad1.GeoPhotoLogger.AppDatabase
import com.example.actividad1.GeoPhotoLogger.RecordAdapter
import com.example.actividad1.GeoPhotoLogger.LocationPhotoRepository
import com.example.actividad1.GeoPhotoLogger.LocationPhotoViewModel
import com.example.actividad1.GeoPhotoLogger.LocationPhotoViewModelFactory
import com.example.actividad1.databinding.ActivityGeoPhotoBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GeoPhotoActivity : AppCompatActivity() {

    // View Binding: Acceso a las vistas del layout activity_main.xml
    private lateinit var binding: ActivityGeoPhotoBinding

    // ViewModel: Contiene la lógica de la UI y los datos. Instanciado usando una factoría.
    private val viewModel: LocationPhotoViewModel by viewModels {
        LocationPhotoViewModelFactory(
            LocationPhotoRepository(AppDatabase.getDatabase(applicationContext).locationPhotoDao())
        )
    }

    // Cliente para obtener la ubicación de Google Play Services
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    // Callback que se ejecutará cuando se reciba una nueva ubicación
    private lateinit var locationCallback: LocationCallback

    // URI temporal para la foto que se va a tomar. Necesario porque la cámara
    // guarda la foto en una URI que le pasamos.
    private var latestPhotoUri: Uri? = null

    // Adaptador para el RecyclerView que mostrará los registros de ubicación/foto
    private lateinit var recordAdapter: RecordAdapter

    // Launcher para solicitar permisos al usuario (ubicación, cámara)
    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            // Verifica si todos los permisos solicitados fueron concedidos
            val allGranted = permissions.entries.all { it.value }
            if (allGranted) {
                Toast.makeText(this, "Permisos concedidos", Toast.LENGTH_SHORT).show()
                startLocationUpdates() // Si se conceden, empieza a obtener ubicación
            } else {
                Toast.makeText(this, "Permisos denegados. Algunas funciones no estarán disponibles.", Toast.LENGTH_LONG).show()
            }
        }

    // Launcher para tomar una foto usando la aplicación de la cámara del sistema
    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) { // Si la foto se tomó con éxito
                latestPhotoUri?.let { uri ->
                    // Carga la miniatura de la foto en el ImageView de la UI
                    Glide.with(this)
                        .load(uri)
                        .centerCrop()
                        .into(binding.imageViewThumbnail)
                    // Guarda la ruta de la foto en la base de datos a través del ViewModel
                    viewModel.savePhoto(uri.toString())
                    Toast.makeText(this, "Foto guardada", Toast.LENGTH_SHORT).show()
                }
            } else { // Si la foto no se tomó (usuario canceló o hubo un error)
                Toast.makeText(this, "No se pudo tomar la foto", Toast.LENGTH_SHORT).show()
                latestPhotoUri = null // Limpia el URI temporal
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Infla el layout y configura el View Binding
        binding = ActivityGeoPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root) // Establece el layout de la actividad

        // Inicializa el cliente de ubicación
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setupLocationCallback()  // Configura el callback para recibir ubicaciones
        setupRecyclerView()      // Configura el RecyclerView
        setupListeners()         // Configura los listeners de los botones
        requestAppPermissions()  // Solicita los permisos necesarios

        // Observa los cambios en la ubicación actual desde el ViewModel
        viewModel.currentLocation.observe(this, Observer { location ->
            location?.let {
                // Actualiza los TextViews con la latitud y longitud
                binding.textViewLatitude.text = String.format(Locale.getDefault(), "Latitud: %.6f", it.latitude)
                binding.textViewLongitude.text = String.format(Locale.getDefault(), "Longitud: %.6f", it.longitude)
            } ?: run {
                // Si no hay ubicación, muestra "N/A"
                binding.textViewLatitude.text = "Latitud: N/A"
                binding.textViewLongitude.text = "Longitud: N/A"
            }
        })

        // Observa todos los registros combinados de la base de datos desde el ViewModel
        viewModel.allCombinedRecords.observe(this, Observer { records ->
            records?.let {
                recordAdapter.submitList(it) // Actualiza la lista en el RecyclerView
            }
        })
    }

    // Configura el callback que se activa con cada nueva ubicación
    private fun setupLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    // Envía la nueva ubicación al ViewModel
                    viewModel.updateCurrentLocation(location)
                }
            }
        }
    }

    // Configura el RecyclerView con su adaptador y layout manager
    private fun setupRecyclerView() {
        recordAdapter = RecordAdapter()
        binding.recyclerViewRecords.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewRecords.adapter = recordAdapter
    }

    // Configura los listeners de clics para los botones
    private fun setupListeners() {
        binding.buttonSaveLocation.setOnClickListener {
            viewModel.saveLocation() // Llama al ViewModel para guardar la ubicación
            Toast.makeText(this, "Ubicación guardada", Toast.LENGTH_SHORT).show()
        }

        binding.buttonTakePhoto.setOnClickListener {
            takePhoto() // Llama a la función para tomar una foto
        }
    }

    // Solicita los permisos de la aplicación al usuario
    private fun requestAppPermissions() {
        val permissionsToRequest = mutableListOf<String>()
        // Verifica si el permiso de ubicación fina no ha sido concedido
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        // Verifica si el permiso de cámara no ha sido concedido
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.CAMERA)
        }

        // Si hay permisos pendientes de solicitud, lánzalos
        if (permissionsToRequest.isNotEmpty()) {
            requestPermissionsLauncher.launch(permissionsToRequest.toTypedArray())
        } else {
            // Si todos los permisos ya están concedidos, inicia las actualizaciones de ubicación
            startLocationUpdates()
        }
    }

    // Inicia las actualizaciones continuas de ubicación
    private fun startLocationUpdates() {
        // Asegúrate de tener el permiso antes de solicitar actualizaciones
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000) // Intervalo de 5 segundos
                //.setWaitForActivityUpdates(false) // No espera por actualizaciones de actividad
                .setMinUpdateIntervalMillis(2000) // Mínimo 2 segundos entre actualizaciones
                .build()

            // Solicita actualizaciones de ubicación
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, mainLooper)
        } else {
            Toast.makeText(this, "Permiso de ubicación no concedido para iniciar actualizaciones.", Toast.LENGTH_SHORT).show()
        }

    }

    // Detiene las actualizaciones de ubicación
    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    // Lógica para tomar una foto
    private fun takePhoto() {
        // Verifica si el permiso de cámara está concedido
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            val photoFile: File? = try {
                createImageFile() // Crea un archivo temporal para la foto
            } catch (ex: Exception) {
                Toast.makeText(this, "Error al crear archivo de imagen: ${ex.message}", Toast.LENGTH_LONG).show()
                null
            }

            photoFile?.also {
                // Obtiene un URI seguro para el archivo de la foto usando FileProvider
                latestPhotoUri = FileProvider.getUriForFile(
                    this,
                    "${applicationContext.packageName}.fileprovider", // Debe coincidir con el "authorities" en AndroidManifest.xml
                    it
                )
                // Lanza la aplicación de la cámara para tomar la foto
                takePictureLauncher.launch(latestPhotoUri!!)
            }
        } else {
            Toast.makeText(this, "Permiso de cámara no concedido.", Toast.LENGTH_SHORT).show()
            requestAppPermissions() // Si no hay permiso, lo solicita
        }
    }

    // Crea un archivo de imagen temporal en el almacenamiento interno privado de la app
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        // Obtiene el directorio de archivos externo específico de la aplicación para imágenes
        // Esto se mapea a /storage/emulated/0/Android/data/com.example.actividad1/files/
        val storageDir: File? = getExternalFilesDir(null) // O getExternalFilesDir(Environment.DIRECTORY_PICTURES) si quieres un subdirectorio estándar de pictures
        // Si usas Environment.DIRECTORY_PICTURES, tu path en file_paths.xml debería ser "."

        // Crea un subdirectorio 'images' dentro de storageDir si no existe
        val imagesDir = File(storageDir, "images")
        if (!imagesDir.exists()) {
            imagesDir.mkdirs() // Crea el directorio y todos los padres necesarios
        }

        // Crea el archivo de imagen dentro del subdirectorio 'images'
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            imagesDir /* directory */ // Este es el directorio 'images' que definimos en file_paths.xml
        )
    }

    override fun onResume() {
        super.onResume()
        // Cuando la actividad vuelve a estar activa, reinicia las actualizaciones de ubicación
        // (solo si los permisos están concedidos)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates()
        }
    }

    override fun onPause() {
        super.onPause()
        // Cuando la actividad se pausa, detiene las actualizaciones de ubicación para ahorrar batería
        stopLocationUpdates()
    }


}
