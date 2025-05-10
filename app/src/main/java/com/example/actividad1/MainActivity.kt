package com.example.actividad1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val btnSend = findViewById<AppCompatButton>(R.id.btnSend)
        val etName = findViewById<AppCompatEditText>(R.id.et_name)

        btnSend.setOnClickListener {
            val name = etName.text.toString()
            if (name.isNotEmpty()) {
                Log.i("Michael", "prueba botton $name")
                val intent = Intent(this, ResultActivity::class.java)
                intent.putExtra("extraName",name)
                startActivity(intent)
            }
        }
    }
}