package com.sakhura.disquera_basica

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sakhura.disquera_basica.adapter.DiscoAdapter
import com.sakhura.disquera_basica.model.Disco
import com.sakhura.disquera_basica.model.DiscosEjemplo
import com.sakhura.disquera_basica.model.GestorUsuarios
import com.sakhura.disquera_basica.model.Usuario

class MainActivity : AppCompatActivity() {

    // Variables para los elementos de la interfaz
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DiscoAdapter
    private lateinit var editTextBuscar: EditText
    private lateinit var btnCarrito: Button
    private lateinit var btnPerfil: Button

    // Lista de todos los discos
    private var todosLosDiscos: List<Disco> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar las vistas
        inicializarVistas()

        // Configurar el RecyclerView
        configurarRecyclerView()

        // Configurar la búsqueda
        configurarBusqueda()

        // Configurar botones
        configurarBotones()

        // Cargar los discos
        cargarDiscos()
    }

    private fun inicializarVistas() {
        recyclerView = findViewById(R.id.rv_discos)
        editTextBuscar = findViewById(R.id.et_buscar)
        btnCarrito = findViewById(R.id.btn_carrito)
        btnPerfil = findViewById(R.id.btn_perfil)
    }

    private fun configurarRecyclerView() {
        // Configurar el layout manager (cómo se organizan los elementos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Crear el adaptador
        adapter = DiscoAdapter(emptyList()) { disco ->
            // Esta función se ejecuta cuando el usuario toca un disco
            mostrarDetallesDisco(disco)
        }

        // Asignar el adaptador al RecyclerView
        recyclerView.adapter = adapter
    }

    private fun configurarBusqueda() {
        editTextBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No necesitamos hacer nada aquí
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Filtrar los discos mientras el usuario escribe
                val termino = s.toString()
                filtrarDiscos(termino)
            }

            override fun afterTextChanged(s: Editable?) {
                // No necesitamos hacer nada aquí
            }
        })
    }

    private fun configurarBotones() {
        btnCarrito.setOnClickListener {
            // Por ahora solo mostramos un mensaje
            Toast.makeText(this, "Función de carrito próximamente!", Toast.LENGTH_SHORT).show()
        }

        btnPerfil.setOnClickListener {
            mostrarMenuPerfil()
        }

        // Actualizar texto del botón según el estado del usuario
        actualizarBotonPerfil()
    }

    private fun actualizarBotonPerfil() {
        val usuario = GestorUsuarios.getUsuarioActual()
        if (usuario != null) {
            btnPerfil.text = "👤 ${usuario.nombre}"
        } else {
            btnPerfil.text = "👤 Iniciar Sesión"
        }
    }

    private fun mostrarMenuPerfil() {
        val usuario = GestorUsuarios.getUsuarioActual()

        if (usuario != null) {
            // Usuario logueado - mostrar opciones de perfil
            val opciones = arrayOf(
                "Ver perfil",
                "Historial de compras",
                "Configuración",
                "Cerrar sesión"
            )

            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Hola, ${usuario.getNombreCompleto()}")
                .setItems(opciones) { _, which ->
                    when (which) {
                        0 -> mostrarPerfil(usuario)
                        1 -> Toast.makeText(this, "Historial próximamente", Toast.LENGTH_SHORT).show()
                        2 -> Toast.makeText(this, "Configuración próximamente", Toast.LENGTH_SHORT).show()
                        3 -> cerrarSesion()
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        } else {
            // Usuario no logueado - ir a login
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun mostrarPerfil(usuario: Usuario) {
        val mensaje = """
            Nombre: ${usuario.getNombreCompleto()}
            Email: ${usuario.email}
            Teléfono: ${if (usuario.telefono.isNotEmpty()) usuario.telefono else "No registrado"}
            Cliente desde: ${java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()).format(usuario.fechaRegistro)}
            Cliente habitual: ${if (usuario.esClienteHabitual) "Sí" else "No"}
        """.trimIndent()

        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Mi Perfil")
            .setMessage(mensaje)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun cerrarSesion() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Cerrar Sesión")
            .setMessage("¿Estás seguro que quieres cerrar sesión?")
            .setPositiveButton("Sí") { _, _ ->
                GestorUsuarios.cerrarSesion()
                actualizarBotonPerfil()
                Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun cargarDiscos() {
        // Obtener los discos de ejemplo
        todosLosDiscos = DiscosEjemplo.obtenerDiscos()

        // Mostrarlos en el RecyclerView
        adapter.actualizarDiscos(todosLosDiscos)
    }

    private fun filtrarDiscos(termino: String) {
        if (termino.isEmpty()) {
            // Si no hay término de búsqueda, mostrar todos los discos
            adapter.actualizarDiscos(todosLosDiscos)
        } else {
            // Filtrar los discos que coincidan con el término
            val discosFiltrados = DiscosEjemplo.buscar(termino)
            adapter.actualizarDiscos(discosFiltrados)
        }
    }

    private fun mostrarDetallesDisco(disco: Disco) {
        // Por ahora solo mostramos los detalles en un Toast
        // Más adelante crearemos una nueva Activity para esto
        val mensaje = """
            ${disco.nombre}
            ${disco.artista}
            ${disco.genero} (${disco.año})
            ${disco.getPrecioFormateado()}
        """.trimIndent()

        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        // Actualizar el botón de perfil cuando regresamos a esta pantalla
        actualizarBotonPerfil()
    }
}