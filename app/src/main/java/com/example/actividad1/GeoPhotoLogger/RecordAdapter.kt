package com.example.actividad1.GeoPhotoLogger

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.actividad1.GeoPhotoLogger.CombinedRecord // Asegúrate de la ruta de tu modelo
import com.example.actividad1.databinding.ItemRecordBinding // <-- ESTA ES LA CORRECTA
import java.text.SimpleDateFormat // <-- Añade esta línea
import java.util.Date           // <-- Añade esta línea
import java.util.Locale         // <-- Añade esta línea


class RecordAdapter : ListAdapter<CombinedRecord, RecordAdapter.RecordViewHolder>(RecordsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val binding = ItemRecordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        val record = getItem(position)
        holder.bind(record)
    }

    inner class RecordViewHolder(private val binding: ItemRecordBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(record: CombinedRecord) {
            binding.textViewLocationData.text = "Lat: %.6f, Lon: %.6f".format(record.latitude, record.longitude)
            binding.textViewLocationTimestamp.text = "Ubicación: ${formatTimestamp(record.locationTimestamp)}"

            if (record.photoPath != null) {
                // Carga la imagen usando Glide.
                // Asegúrate de que photoPath es un String que representa una URI válida.
                // Si photoPath puede ser null, debes manejarlo.
                Glide.with(binding.imageViewRecordThumbnail.context)
                    .load(record.photoPath) // <-- AQUI ES DONDE SE PASA LA URI
                    .centerCrop()
                    .placeholder(android.R.drawable.ic_menu_gallery) // Placeholder opcional
                    .error(android.R.drawable.ic_delete) // Imagen de error opcional
                    .into(binding.imageViewRecordThumbnail)
            } else {
                // Si no hay foto, puedes limpiar la ImageView o establecer una imagen por defecto
                binding.imageViewRecordThumbnail.setImageDrawable(null) // O establece un drawable por defecto
            }
            binding.textViewPhotoTimestamp.text = record.photoTimestamp?.let { "Foto: ${formatTimestamp(it)}" } ?: "Foto: N/A"
        }

        private fun formatTimestamp(timestamp: Long): String {
            return SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date(timestamp))
        }
    }

    private class RecordsDiffCallback : DiffUtil.ItemCallback<CombinedRecord>() {
        override fun areItemsTheSame(oldItem: CombinedRecord, newItem: CombinedRecord): Boolean {
            return oldItem.locationRecordId == newItem.locationRecordId
        }

        override fun areContentsTheSame(oldItem: CombinedRecord, newItem: CombinedRecord): Boolean {
            return oldItem == newItem
        }
    }
}