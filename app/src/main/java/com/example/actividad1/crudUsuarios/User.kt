package com.example.actividad1.crudUsuarios

import java.util.UUID

data class User(
    val id: String = UUID.randomUUID().toString(), // ID único para cada usuario
    var name: String,
    var email: String,
    var phone: String
)