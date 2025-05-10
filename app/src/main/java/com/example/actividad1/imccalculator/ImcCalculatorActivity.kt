package com.example.actividad1.imccalculator

import android.content.Intent
import android.icu.text.DecimalFormat
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.actividad1.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.slider.RangeSlider

class ImcCalculatorActivity : AppCompatActivity() {

    private var isMaleSelected: Boolean = true
    private var isFemaleSelected: Boolean = false
    private var currentHeight: Int = 120
    private var currentWeight: Int = 50
    private var currentAge: Int = 30


    private lateinit var viewMale: CardView
    private lateinit var viewFemale: CardView
    private lateinit var tvHeight: TextView
    private lateinit var rsHeight: RangeSlider
    private lateinit var btnSustractWeight: FloatingActionButton
    private lateinit var btnPlusWeight: FloatingActionButton
    private lateinit var tvWeight: TextView
    private lateinit var btnSustractAge: FloatingActionButton
    private lateinit var btnPlusAge: FloatingActionButton
    private lateinit var tvAge: TextView
    private lateinit var btnCalculate: AppCompatButton

    companion object{
        const val INT_KEY = "IMC_RESULT"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_imc_calculator)
        initComponent()
        initListeners()
        initUI()
    }

    private fun initComponent() {
        viewMale = findViewById<CardView>(R.id.viewMale)
        viewFemale = findViewById<CardView>(R.id.viewFemale)
        tvHeight = findViewById<TextView>(R.id.tvHeight)
        rsHeight = findViewById<RangeSlider>(R.id.rsHeight)
        btnPlusWeight = findViewById<FloatingActionButton>(R.id.fabPlusWeight)
        btnSustractWeight = findViewById<FloatingActionButton>(R.id.fabSubstractWeight)
        tvWeight = findViewById<TextView>(R.id.tvWeight)
        btnSustractAge = findViewById<FloatingActionButton>(R.id.fabSubstractAge)
        btnPlusAge = findViewById<FloatingActionButton>(R.id.fabPlusAge)
        tvAge = findViewById<TextView>(R.id.tvAge)
        btnCalculate = findViewById<AppCompatButton>(R.id.btnCalculate)
    }

    private fun initListeners() {
        viewMale.setOnClickListener {
            changeGender()
            setGenderColor()
        }
        viewFemale.setOnClickListener {
            changeGender()
            setGenderColor()
        }
        rsHeight.addOnChangeListener { _, value, _ ->
            val df = DecimalFormat("#.##")
            currentHeight = df.format(value).toInt()
            tvHeight.text = "$currentHeight cm"
        }
        btnPlusWeight.setOnClickListener {
            currentWeight += 1
            setWeight()
        }
        btnSustractWeight.setOnClickListener {
            currentWeight -= 1
            setWeight()
        }
        btnPlusAge.setOnClickListener {
            currentAge += 1
            setAge()
        }
        btnSustractAge.setOnClickListener {
            currentAge -= 1
            setAge()
        }
        btnCalculate.setOnClickListener {
            val resultImc = calculateIMC()
            navegateToResult(resultImc)
        }
    }

    private fun navegateToResult(resultImc: Double) {
        val intent = Intent(this, resultImcActivity::class.java)
        intent.putExtra(INT_KEY, resultImc)
        startActivity(intent)
    }

    private fun calculateIMC(): Double {
        val IMC = currentWeight / (currentHeight.toDouble() / 100 * currentHeight.toDouble() / 100)
        val df = DecimalFormat("#.##")
        return df.format(IMC).toDouble()
    }

    private fun setWeight() {
        tvWeight.text = currentWeight.toString()
    }

    private fun setAge() {
        tvAge.text = currentAge.toString()
    }

    private fun changeGender() {
        isMaleSelected = !isMaleSelected
        isFemaleSelected = !isFemaleSelected
    }

    private fun setGenderColor() {
        viewMale.setBackgroundColor(getBackgroundColor(isMaleSelected))
        viewFemale.setBackgroundColor(getBackgroundColor(isFemaleSelected))
    }

    private fun getBackgroundColor(isSelectedComponet: Boolean): Int {
        val colorReference = if (isSelectedComponet) {
            R.color.bg_component_selected
        } else {
            R.color.bg_component
        }

        return ContextCompat.getColor(this, colorReference)
    }

    private fun initUI() {
        setGenderColor()
        setWeight()
        setAge()
    }

}