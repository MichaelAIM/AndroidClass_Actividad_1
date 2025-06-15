package com.example.actividad1.crudUsuarios

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.actividad1.R
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.app.Activity
import android.content.Intent

class UsuariosActivity : AppCompatActivity() {

    private lateinit var userDataSource: UserDataSource
    private lateinit var userAdapter: UserAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var fabAdd: FloatingActionButton

    // ActivityResultLauncher para manejar los resultados de AddEditUserActivity
    private val addEditUserLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Si el usuario se añadió/editó con éxito, recargamos la lista
            loadUsers()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usuarios)

        userDataSource = UserDataSource(this)
        recyclerView = findViewById(R.id.recyclerViewUsers)
        fabAdd = findViewById(R.id.floatingActionButton)

        setupRecyclerView()
        loadUsers()

        fabAdd.setOnClickListener {
            val intent = Intent(this, AddEditUserActivity::class.java)
            addEditUserLauncher.launch(intent)
        }
    }

    private fun setupRecyclerView() {
        userAdapter = UserAdapter(
            mutableListOf(), // Se inicializa con una lista vacía, se actualizará al cargar
            onEditClick = { user ->
                // Acción para editar
                val intent = Intent(this, AddEditUserActivity::class.java).apply {
                    putExtra(Constants.EXTRA_USER_ID, user.id)
                }
                addEditUserLauncher.launch(intent)
            },
            onDeleteClick = { user ->
                // Acción para eliminar
                showDeleteConfirmationDialog(user)
            }
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = userAdapter
    }

    private fun loadUsers() {
        val users = userDataSource.getAllUsers()
        userAdapter.updateUsers(users)
        if (users.isEmpty()) {
            Toast.makeText(this, "No hay usuarios. ¡Añade uno!", Toast.LENGTH_LONG).show()
        }
    }

    private fun showDeleteConfirmationDialog(user: User) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Usuario")
            .setMessage("¿Estás seguro de que quieres eliminar a '${user.name}'?")
            .setPositiveButton("Sí") { dialog, _ ->
                userDataSource.deleteUser(user.id)
                loadUsers() // Recarga la lista después de eliminar
                Toast.makeText(this, "Usuario eliminado: ${user.name}", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

}