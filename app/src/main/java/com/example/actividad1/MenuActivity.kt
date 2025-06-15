package com.example.actividad1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.actividad1.GeoPhotoLogger.GeoPhotoActivity
import com.example.actividad1.crudUsuarios.UsuariosActivity
import com.example.actividad1.imccalculator.ImcBasicActivity
import com.example.actividad1.imccalculator.ImcCalculatorActivity

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)
        val btnSaludaap = findViewById<Button>(R.id.mn_btn_1)
        val btnIMCApp = findViewById<Button>(R.id.mn_btn_2)
        val btnIMCBasicApp = findViewById<Button>(R.id.mn_btn_3)
        val btnManUser = findViewById<Button>(R.id.mn_btn_4)
        val btnGeoPhotoApp = findViewById<Button>(R.id.mn_btn_5)

        btnSaludaap.setOnClickListener{navegarApp()}
        btnIMCApp.setOnClickListener{navegarIMCApp()}
        btnIMCBasicApp.setOnClickListener { navegarIMCBasicApp() }
        btnManUser.setOnClickListener { navegarUsuarios() }
        btnGeoPhotoApp.setOnClickListener { navegarGeoPhotoApp() }

    }

    private fun navegarIMCApp() {
        val intent = Intent(this, ImcCalculatorActivity::class.java)
        startActivity(intent)
    }

    private fun navegarUsuarios() {
        val intent = Intent(this, UsuariosActivity::class.java)
        startActivity(intent)
    }


    private fun navegarApp(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun navegarIMCBasicApp(){
        val intent = Intent(this, ImcBasicActivity::class.java)
        startActivity(intent)
    }

    private fun navegarGeoPhotoApp(){
        val intent = Intent(this, GeoPhotoActivity::class.java)
        startActivity(intent)
    }
}