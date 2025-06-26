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

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnLogin: Button
    private lateinit var btnIrRegistro: Button
    private lateinit var tvContinuarSinCuenta: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Agregar usuarios de ejemplo para testing
        GestorUsuarios.agregarUsuariosEjemplo()

        // Verificar si ya hay usuario logueado
        if (GestorUsuarios.hayUsuarioLogueado()) {
            irAMainActivity()
            return
        }

        inicializarVistas()
        configurarEventos()
    }

    private fun inicializarVistas() {
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        btnLogin = findViewById(R.id.btn_login)
        btnIrRegistro = findViewById(R.id.btn_ir_registro)
        tvContinuarSinCuenta = findViewById(R.id.tv_continuar_sin_cuenta)
    }

    private fun configurarEventos() {
        btnLogin.setOnClickListener {
            realizarLogin()
        }

        btnIrRegistro.setOnClickListener {
            irARegistro()
        }

        tvContinuarSinCuenta.setOnClickListener {
            irAMainActivitySinLogin()
        }
    }

    private fun realizarLogin() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        // Validación básica en el cliente
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Intentar login
        when (val resultado = GestorUsuarios.iniciarSesion(email, password)) {
            is Resultado.Exito -> {
                Toast.makeText(this, "¡Bienvenido ${resultado.usuario?.nombre}!", Toast.LENGTH_SHORT).show()
                irAMainActivity()
            }
            is Resultado.Error -> {
                Toast.makeText(this, resultado.mensaje, Toast.LENGTH_LONG).show()

                // Limpiar contraseña si el login falló
                etPassword.text?.clear()
            }
        }
    }

    private fun irARegistro() {
        val intent = Intent(this, RegistroActivity::class.java)
        startActivity(intent)
    }

    private fun irAMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Para que no pueda volver atrás con el botón back
    }

    private fun irAMainActivitySinLogin() {
        Toast.makeText(this, "Continuando como invitado", Toast.LENGTH_SHORT).show()
        irAMainActivity()
    }

    // Método que se llama cuando regresa de la pantalla de registro
    override fun onResume() {
        super.onResume()

        // Si se registró exitosamente y hay usuario logueado, ir a main
        if (GestorUsuarios.hayUsuarioLogueado()) {
            irAMainActivity()
        }
    }
}