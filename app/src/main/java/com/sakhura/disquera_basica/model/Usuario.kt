package com.sakhura.disquera_basica.model

import java.util.Date

// Modelo para representar un usuario
data class Usuario(
    val id: String,
    val nombre: String,
    val apellido: String,
    val email: String,
    val password: String, // En una app real, esto estaría encriptado
    val telefono: String = "",
    val fechaRegistro: Date = Date(),
    val esClienteHabitual: Boolean = false
) {
    // Función para obtener el nombre completo
    fun getNombreCompleto(): String {
        return "$nombre $apellido"
    }

    // Función para verificar si la contraseña es correcta
    fun verificarPassword(password: String): Boolean {
        return this.password == password
    }
}

// Gestor de usuarios (simulando una base de datos)
object GestorUsuarios {

    // Lista de usuarios registrados (en memoria por ahora)
    private val usuarios = mutableListOf<Usuario>()

    // Usuario actual logueado
    private var usuarioActual: Usuario? = null

    // Registrar un nuevo usuario
    fun registrarUsuario(
        nombre: String,
        apellido: String,
        email: String,
        password: String,
        telefono: String = ""
    ): Resultado {

        // Validaciones básicas
        if (nombre.isBlank() || apellido.isBlank()) {
            return Resultado.Error("El nombre y apellido son obligatorios")
        }

        if (email.isBlank() || !email.contains("@")) {
            return Resultado.Error("Email inválido")
        }

        if (password.length < 6) {
            return Resultado.Error("La contraseña debe tener al menos 6 caracteres")
        }

        // Verificar si el email ya existe
        if (usuarios.any { it.email == email }) {
            return Resultado.Error("Este email ya está registrado")
        }

        // Crear el nuevo usuario
        val nuevoUsuario = Usuario(
            id = generarId(),
            nombre = nombre,
            apellido = apellido,
            email = email,
            password = password,
            telefono = telefono,
            fechaRegistro = Date(),
            esClienteHabitual = false
        )

        // Agregarlo a la lista
        usuarios.add(nuevoUsuario)

        return Resultado.Exito("Usuario registrado exitosamente", nuevoUsuario)
    }

    // Iniciar sesión
    fun iniciarSesion(email: String, password: String): Resultado {

        if (email.isBlank() || password.isBlank()) {
            return Resultado.Error("Email y contraseña son obligatorios")
        }

        // Buscar el usuario
        val usuario = usuarios.find { it.email == email }

        if (usuario == null) {
            return Resultado.Error("Usuario no encontrado")
        }

        if (!usuario.verificarPassword(password)) {
            return Resultado.Error("Contraseña incorrecta")
        }

        // Guardar como usuario actual
        usuarioActual = usuario

        return Resultado.Exito("Login exitoso", usuario)
    }

    // Cerrar sesión
    fun cerrarSesion() {
        usuarioActual = null
    }

    // Obtener usuario actual
    fun getUsuarioActual(): Usuario? {
        return usuarioActual
    }

    // Verificar si hay usuario logueado
    fun hayUsuarioLogueado(): Boolean {
        return usuarioActual != null
    }

    // Obtener todos los usuarios (para debug)
    fun getTodosLosUsuarios(): List<Usuario> {
        return usuarios.toList()
    }

    // Función auxiliar para generar IDs únicos
    private fun generarId(): String {
        return "user_${System.currentTimeMillis()}"
    }

    // Agregar algunos usuarios de ejemplo para testing
    fun agregarUsuariosEjemplo() {
        if (usuarios.isEmpty()) {
            registrarUsuario(
                nombre = "Juan",
                apellido = "Pérez",
                email = "juan@example.com",
                password = "123456",
                telefono = "555-1234"
            )

            registrarUsuario(
                nombre = "María",
                apellido = "González",
                email = "maria@example.com",
                password = "123456",
                telefono = "555-5678"
            )
        }
    }
}

// Clase para manejar resultados de operaciones
sealed class Resultado {
    data class Exito(val mensaje: String, val usuario: Usuario? = null) : Resultado()
    data class Error(val mensaje: String) : Resultado()
}