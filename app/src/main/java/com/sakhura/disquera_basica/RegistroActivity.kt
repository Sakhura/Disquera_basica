

package com.sakhura.disquera_basica

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.sakhura.disquera_basica.model.GestorUsuarios
import com.sakhura.disquera_basica.model.Resultado

class RegistroActivity : AppCompatActivity() {

    private lateinit var etNombre: TextInputEditText
    private lateinit var etApellido: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var etConfirmarPassword: TextInputEditText
    private lateinit var etTelefono: TextInputEditText
    private lateinit var btnRegistrar: Button
    private lateinit var btnVolver: TextView
    private lateinit var tvIrLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        inicializarVistas()
        configurarEventos()
    }

    private fun inicializarVistas() {
        etNombre = findViewById(R.id.et_nombre)
        etApellido = findViewById(R.id.et_apellido)
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        etConfirmarPassword = findViewById(R.id.et_confirmar_password)
        etTelefono = findViewById(R.id.et_telefono)
        btnRegistrar = findViewById(R.id.btn_registrar)
        btnVolver = findViewById(R.id.btn_volver)
        tvIrLogin = findViewById(R.id.tv_ir_login)
    }

    private fun configurarEventos() {
        btnRegistrar.setOnClickListener {
            realizarRegistro()
        }

        btnVolver.setOnClickListener {
            finish() // Volver a la pantalla anterior
        }

        tvIrLogin.setOnClickListener {
            irALogin()
        }
    }

    private fun realizarRegistro() {
        // Obtener datos del formulario
        val nombre = etNombre.text.toString().trim()
        val apellido = etApellido.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val confirmarPassword = etConfirmarPassword.text.toString().trim()
        val telefono = etTelefono.text.toString().trim()

        // Validaciones en el cliente
        if (!validarFormulario(nombre, apellido, email, password, confirmarPassword)) {
            return
        }

        // Intentar registrar
        when (val resultado = GestorUsuarios.registrarUsuario(
            nombre = nombre,
            apellido = apellido,
            email = email,
            password = password,
            telefono = telefono
        )) {
            is Resultado.Exito -> {
                Toast.makeText(this, "¡Cuenta creada exitosamente!", Toast.LENGTH_LONG).show()

                // Iniciar sesión automáticamente después del registro
                GestorUsuarios.iniciarSesion(email, password)

                // Ir a la pantalla principal
                irAMainActivity()
            }
            is Resultado.Error -> {
                Toast.makeText(this, resultado.mensaje, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun validarFormulario(
        nombre: String,
        apellido: String,
        email: String,
        password: String,
        confirmarPassword: String
    ): Boolean {

        // Verificar campos obligatorios
        if (nombre.isEmpty()) {
            etNombre.error = "El nombre es obligatorio"
            etNombre.requestFocus()
            return false
        }

        if (apellido.isEmpty()) {
            etApellido.error = "El apellido es obligatorio"
            etApellido.requestFocus()
            return false
        }

        if (email.isEmpty()) {
            etEmail.error = "El email es obligatorio"
            etEmail.requestFocus()
            return false
        }

        // Validar formato de email básico
        if (!email.contains("@") || !email.contains(".")) {
            etEmail.error = "Email inválido"
            etEmail.requestFocus()
            return false
        }

        if (password.isEmpty()) {
            etPassword.error = "La contraseña es obligatoria"
            etPassword.requestFocus()
            return false
        }

        if (password.length < 6) {
            etPassword.error = "La contraseña debe tener al menos 6 caracteres"
            etPassword.requestFocus()
            return false
        }

        if (password != confirmarPassword) {
            etConfirmarPassword.error = "Las contraseñas no coinciden"
            etConfirmarPassword.requestFocus()
            return false
        }

        return true
    }

    private fun irALogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun irAMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Para que no pueda volver atrás
    }
}