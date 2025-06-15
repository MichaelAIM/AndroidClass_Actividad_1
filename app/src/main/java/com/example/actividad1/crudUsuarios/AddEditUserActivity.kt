package com.example.actividad1.crudUsuarios

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.actividad1.R

class AddEditUserActivity : AppCompatActivity() {

    private lateinit var userDataSource: UserDataSource
    private var userId: String? = null // Para saber si estamos editando

    private lateinit var editUserName: EditText
    private lateinit var editUserEmail: EditText
    private lateinit var editUserPhone: EditText
    private lateinit var buttonSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_user)

        userDataSource = UserDataSource(this)

        editUserName = findViewById(R.id.editTextUserName)
        editUserEmail = findViewById(R.id.editTextUserEmail)
        editUserPhone = findViewById(R.id.editTextUserPhone)
        buttonSave = findViewById(R.id.buttonSaveUser)

        // Comprueba si estamos en modo edición (se pasó un ID de usuario)
        userId = intent.getStringExtra(Constants.EXTRA_USER_ID)
        if (userId != null) {
            // Cargar datos del usuario para editar
            val user = userDataSource.getUserById(userId!!)
            user?.let {
                editUserName.setText(it.name)
                editUserEmail.setText(it.email)
                editUserPhone.setText(it.phone)
                title = "Editar Usuario"
            } ?: run {
                Toast.makeText(this, "Usuario no encontrado.", Toast.LENGTH_SHORT).show()
                finish() // Cierra la actividad si el usuario no existe
            }
        } else {
            title = "Añadir Usuario"
        }

        buttonSave.setOnClickListener {
            saveUser()
        }
    }

    private fun saveUser() {
        val name = editUserName.text.toString().trim()
        val email = editUserEmail.text.toString().trim()
        val phone = editUserPhone.text.toString().trim()

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        if (userId == null) {
            // Añadir nuevo usuario
            val newUser = User(name = name, email = email, phone = phone)
            userDataSource.addUser(newUser)
            setResult(Activity.RESULT_OK) // Indica que se realizó una acción exitosa
        } else {
            // Actualizar usuario existente
            val updatedUser = User(id = userId!!, name = name, email = email, phone = phone)
            userDataSource.updateUser(updatedUser)
            setResult(Activity.RESULT_OK) // Indica que se realizó una acción exitosa
        }
        finish() // Cierra la actividad
    }
}
