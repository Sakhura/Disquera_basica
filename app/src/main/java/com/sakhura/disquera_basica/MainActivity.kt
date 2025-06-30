package com.sakhura.disquera_basica

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sakhura.disquera_basica.adapter.DiscoAdapter
import com.sakhura.disquera_basica.model.Disco
import com.sakhura.disquera_basica.model.GestorCarrito
import com.sakhura.disquera_basica.model.GestorUsuarios

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DiscoAdapter
    private lateinit var tvBienvenida: TextView
    private lateinit var etBuscar: EditText
    private lateinit var btnCarrito: Button
    private lateinit var btnCerrarSesion: Button

    private var discosTodos = listOf<Disco>()
    private var discosFiltrados = listOf<Disco>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inicializarVistas()
        configurarDatos()
        configurarRecyclerView()
        configurarBuscador()
        configurarEventos()
        configurarCarrito()
        actualizarCarrito()
    }

    private fun inicializarVistas() {
        recyclerView = findViewById(R.id.rv_discos)
        tvBienvenida = findViewById(R.id.tv_bienvenida)
        etBuscar = findViewById(R.id.et_buscar)
        btnCarrito = findViewById(R.id.btn_carrito)
        btnCerrarSesion = findViewById(R.id.btn_cerrar_sesion)

        // Mostrar nombre del usuario logueado
        val usuario = GestorUsuarios.getUsuarioActual()
        tvBienvenida.text = "¡Hola ${usuario?.nombre ?: "Usuario"}! 🎵"
    }

    private fun configurarDatos() {
        discosTodos = listOf(
            Disco(1, "The Dark Side of the Moon", "Pink Floyd", "Rock Progresivo", 1973, 29.99),
            Disco(2, "Abbey Road", "The Beatles", "Rock", 1969, 27.50),
            Disco(3, "Thriller", "Michael Jackson", "Pop", 1982, 25.99),
            Disco(4, "Back in Black", "AC/DC", "Hard Rock", 1980, 24.99),
            Disco(5, "Hotel California", "Eagles", "Rock", 1976, 26.99),
            Disco(6, "Nevermind", "Nirvana", "Grunge", 1991, 23.99),
            Disco(7, "The Wall", "Pink Floyd", "Rock Progresivo", 1979, 31.99),
            Disco(8, "Led Zeppelin IV", "Led Zeppelin", "Hard Rock", 1971, 28.50),
            Disco(9, "OK Computer", "Radiohead", "Rock Alternativo", 1997, 22.99),
            Disco(10, "Kind of Blue", "Miles Davis", "Jazz", 1959, 32.99),
            Disco(11, "Purple Rain", "Prince", "Pop/Funk", 1984, 24.50),
            Disco(12, "Born to Run", "Bruce Springsteen", "Rock", 1975, 25.50),
            Disco(13, "Rumours", "Fleetwood Mac", "Rock", 1977, 26.50),
            Disco(14, "The Joshua Tree", "U2", "Rock", 1987, 23.50),
            Disco(15, "Appetite for Destruction", "Guns N' Roses", "Hard Rock", 1987, 24.99)
        )

        discosFiltrados = discosTodos
    }

    private fun configurarRecyclerView() {
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        adapter = DiscoAdapter(
            discos = discosFiltrados,
            onDiscoClick = { disco ->
                // Agregar al carrito cuando se hace click en un disco
                val agregado = GestorCarrito.agregarDisco(disco)
                if (agregado) {
                    Toast.makeText(this, "\"${disco.nombre}\" agregado al carrito", Toast.LENGTH_SHORT).show()
                    actualizarCarrito()
                } else {
                    Toast.makeText(this, "Error al agregar al carrito", Toast.LENGTH_SHORT).show()
                }
            }
        )

        recyclerView.adapter = adapter
    }

    private fun configurarBuscador() {
        etBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filtrarDiscos(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun configurarEventos() {
        btnCerrarSesion.setOnClickListener {
            GestorUsuarios.cerrarSesion()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun configurarCarrito() {
        btnCarrito.setOnClickListener {
            val intent = Intent(this, CarritoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun filtrarDiscos(consulta: String) {
        discosFiltrados = if (consulta.isBlank()) {
            discosTodos
        } else {
            discosTodos.filter { disco ->
                disco.nombre.contains(consulta, ignoreCase = true) ||
                        disco.artista.contains(consulta, ignoreCase = true) ||
                        disco.genero.contains(consulta, ignoreCase = true)
            }
        }

        adapter.actualizarDiscos(discosFiltrados)
    }

    private fun actualizarCarrito() {
        val resumen = GestorCarrito.getResumenCarrito()
        btnCarrito.text = "🛒 $resumen"
    }

    override fun onResume() {
        super.onResume()
        actualizarCarrito()
    }
}