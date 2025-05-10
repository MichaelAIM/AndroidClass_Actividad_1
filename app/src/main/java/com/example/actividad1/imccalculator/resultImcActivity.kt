package com.example.actividad1.imccalculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.actividad1.R
import com.example.actividad1.imccalculator.ImcCalculatorActivity.Companion.INT_KEY

class resultImcActivity : AppCompatActivity() {

    private lateinit var tvResultad: TextView
    private lateinit var tvIMC: TextView
    private lateinit var tvDescriptioon: TextView
    private lateinit var btnRecalculate: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_result_imc)
        val result: Double = intent.extras?.getDouble(INT_KEY) ?: -1.0
        initComponeets()
        initListeners()
        initUI(result)
    }

    private fun initListeners() {
        btnRecalculate.setOnClickListener { onBackPressed() }
    }

    private fun initUI(result: Double) {
        tvIMC.text = result.toString()

        when (result) {
            in 0.00..18.50 -> {
                tvResultad.text = getString(R.string.title_bajo_peso)
                tvDescriptioon.text = getString(R.string.description_bajo_peso)
                tvResultad.setTextColor(ContextCompat.getColor(this, R.color.peso_bajo))
            }

            in 18.51..24.99 -> {
                tvResultad.text = getString(R.string.title_peso_normal)
                tvResultad.setTextColor(ContextCompat.getColor(this, R.color.peso_normal))
                tvDescriptioon.text = getString(R.string.description_peso_normal)
            }

            in 25.00..29.99 -> {
                tvResultad.text = getString(R.string.title_sobrepeso)
                tvResultad.setTextColor(ContextCompat.getColor(this, R.color.sobrepeso))
                tvDescriptioon.text = getString(R.string.description_sobrepeso)
            }

            in 30.00..99.00 -> {
                tvResultad.text = getString(R.string.title_obesidad)
                tvResultad.setTextColor(ContextCompat.getColor(this, R.color.obesidad))
                tvDescriptioon.text = getString(R.string.description_obesidad)
            }

            else -> {
                tvIMC.text = getString(R.string.error)
                tvResultad.text = getString(R.string.error)
                tvDescriptioon.text = getString(R.string.error)

            }
        }
    }

    private fun initComponeets() {
        tvIMC = findViewById(R.id.tvIMC)
        tvResultad = findViewById(R.id.tvResult)
        tvDescriptioon = findViewById(R.id.tvDescription)
        btnRecalculate = findViewById(R.id.btnRecalculate)
    }


}