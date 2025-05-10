package com.example.actividad1.imccalculator

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.actividad1.R

class ImcBasicActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_imc_basic)

        val weightInput = findViewById<EditText>(R.id.weightInput)
        val heightInput = findViewById<EditText>(R.id.heightInput)
        val calculateButton = findViewById<Button>(R.id.calculateButton)
        val resultText = findViewById<TextView>(R.id.resultText)
        val categoryText = findViewById<TextView>(R.id.categoryText)

        calculateButton.setOnClickListener {
            val weightStr = weightInput.text.toString()
            val heightStr = heightInput.text.toString()

            if (weightStr.isNotEmpty() && heightStr.isNotEmpty()) {
                val weight = weightStr.toDouble()
                val height = heightStr.toDouble()

                if (weight > 0 && height > 0) {
                    val bmi = calculateBMI(weight, height)
                    val category = determineCategory(bmi)

                    resultText.text = "Tu IMC es: %.2f".format(bmi)
                    categoryText.text = "Categoría: $category"
                } else {
                    Toast.makeText(this, "Los valores deben ser mayores a cero", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(this, "Por favor ingresa ambos valores", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun calculateBMI(weight: Double, height: Double): Double {
        return weight / (height * height)
    }

    private fun determineCategory(bmi: Double): String {
        return when {
            bmi < 18.5 -> "Bajo peso"
            bmi < 24.9 -> "Peso normal"
            bmi < 29.9 -> "Sobrepeso"
            else -> "Obesidad"
        }
    }
}