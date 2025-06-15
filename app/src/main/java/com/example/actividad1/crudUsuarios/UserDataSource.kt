package com.example.actividad1.crudUsuarios

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class UserDataSource(private val context: Context) {

    private val gson = Gson()
    private val file = File(context.filesDir, Constants.FILE_NAME)

    // Leer todos los usuarios del JSON
    fun getAllUsers(): MutableList<User> {
        if (!file.exists()) {
            return mutableListOf()
        }
        val type = object : TypeToken<MutableList<User>>() {}.type
        return try {
            FileReader(file).use { reader ->
                gson.fromJson(reader, type) ?: mutableListOf()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            mutableListOf()
        }
    }

    // Guardar una lista de usuarios en el JSON
    fun saveAllUsers(users: List<User>) {
        try {
            FileWriter(file).use { writer ->
                gson.toJson(users, writer)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Añadir un nuevo usuario
    fun addUser(user: User) {
        val users = getAllUsers()
        users.add(user)
        saveAllUsers(users)
    }

    // Actualizar un usuario existente
    fun updateUser(updatedUser: User) {
        val users = getAllUsers()
        val index = users.indexOfFirst { it.id == updatedUser.id }
        if (index != -1) {
            users[index] = updatedUser
            saveAllUsers(users)
        }
    }

    // Eliminar un usuario
    fun deleteUser(userId: String) {
        val users = getAllUsers()
        val initialSize = users.size
        users.removeAll { it.id == userId }
        if (users.size < initialSize) { // Solo guarda si se eliminó algo
            saveAllUsers(users)
        }
    }

    // Obtener un usuario por ID (útil para la edición)
    fun getUserById(userId: String): User? {
        return getAllUsers().find { it.id == userId }
    }
}