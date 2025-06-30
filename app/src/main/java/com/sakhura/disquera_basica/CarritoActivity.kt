// Archivo: app/src/main/java/com/sakhura/disquera_basica/CarritoActivity.kt
// Versión simplificada para evitar errores de sintaxis

package com.sakhura.disquera_basica

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sakhura.disquera_basica.adapter.CarritoAdapter
import com.sakhura.disquera_basica.model.GestorCarrito
import com.sakhura.disquera_basica.model.ResultadoCompra

class CarritoActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CarritoAdapter
    private lateinit var layoutCarritoVacio: LinearLayout
    private lateinit var layoutPanelInferior: LinearLayout
    private lateinit var tvTotalItems: TextView
    private lateinit var tvTotalPagar: TextView
    private lateinit var btnVolver: TextView
    private lateinit var btnVaciarCarrito: Button
    private lateinit var btnComprar: Button
    private lateinit var btnIrCatalogo: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)

        inicializarVistas()
        configurarRecyclerView()
        configurarEventos()
        actualizarInterfaz()
    }

    private fun inicializarVistas() {
        recyclerView = findViewById(R.id.rv_carrito)
        layoutCarritoVacio = findViewById(R.id.layout_carrito_vacio)
        layoutPanelInferior = findViewById(R.id.layout_panel_inferior)
        tvTotalItems = findViewById(R.id.tv_total_items)
        tvTotalPagar = findViewById(R.id.tv_total_pagar)
        btnVolver = findViewById(R.id.btn_volver_carrito)
        btnVaciarCarrito = findViewById(R.id.btn_vaciar_carrito)
        btnComprar = findViewById(R.id.btn_comprar)
        btnIrCatalogo = findViewById(R.id.btn_ir_catalogo)
    }

    private fun configurarRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = CarritoAdapter(
            items = GestorCarrito.getItems(),
            onCantidadChanged = { discoId, nuevaCantidad ->
                GestorCarrito.actualizarCantidad(discoId, nuevaCantidad)
                actualizarInterfaz()
                Toast.makeText(this, "Cantidad actualizada", Toast.LENGTH_SHORT).show()
            },
            onEliminarItem = { discoId ->
                mostrarDialogoEliminar(discoId)
            }
        )

        recyclerView.adapter = adapter
    }

    private fun configurarEventos() {
        btnVolver.setOnClickListener {
            finish()
        }

        btnVaciarCarrito.setOnClickListener {
            mostrarDialogoVaciarCarrito()
        }

        btnComprar.setOnClickListener {
            realizarCompra()
        }

        btnIrCatalogo.setOnClickListener {
            finish()
        }
    }

    private fun actualizarInterfaz() {
        val items = GestorCarrito.getItems()

        if (GestorCarrito.estaVacio()) {
            recyclerView.visibility = View.GONE
            layoutPanelInferior.visibility = View.GONE
            layoutCarritoVacio.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            layoutPanelInferior.visibility = View.VISIBLE
            layoutCarritoVacio.visibility = View.GONE

            adapter.actualizarItems(items)

            val cantidadTotal = GestorCarrito.getCantidadTotalItems()
            val total = GestorCarrito.getTotal()

            tvTotalItems.text = cantidadTotal.toString() + " items"
            tvTotalPagar.text = String.format("$%.2f", total)
        }
    }

    private fun mostrarDialogoEliminar(discoId: Int) {
        val disco = GestorCarrito.getItems().find { it.disco.id == discoId }?.disco

        AlertDialog.Builder(this)
            .setTitle("Eliminar del carrito")
            .setMessage("¿Eliminar del carrito?")
            .setPositiveButton("Eliminar") { _, _ ->
                GestorCarrito.removerDisco(discoId)
                actualizarInterfaz()
                Toast.makeText(this, "Disco eliminado del carrito", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun mostrarDialogoVaciarCarrito() {
        AlertDialog.Builder(this)
            .setTitle("Vaciar carrito")
            .setMessage("¿Estás seguro que quieres vaciar todo el carrito?")
            .setPositiveButton("Vaciar") { _, _ ->
                GestorCarrito.vaciar()
                actualizarInterfaz()
                Toast.makeText(this, "Carrito vaciado", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun realizarCompra() {
        val totalFormateado = String.format("$%.2f", GestorCarrito.getTotal())

        AlertDialog.Builder(this)
            .setTitle("Confirmar compra")
            .setMessage("¿Proceder con la compra por " + totalFormateado + "?")
            .setPositiveButton("Comprar") { _, _ ->
                when (val resultado = GestorCarrito.realizarCompra()) {
                    is ResultadoCompra.Exito -> {
                        mostrarDialogoCompraExitosa(resultado)
                    }
                    is ResultadoCompra.Error -> {
                        Toast.makeText(this, resultado.mensaje, Toast.LENGTH_LONG).show()
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun mostrarDialogoCompraExitosa(resultado: ResultadoCompra.Exito) {
        val mensaje = resultado.mensaje + "\n\n" + resultado.pedido.getResumen() + "\n\n¡Gracias por tu compra!"

        AlertDialog.Builder(this)
            .setTitle("Compra Exitosa")
            .setMessage(mensaje)
            .setPositiveButton("Aceptar") { _, _ ->
                actualizarInterfaz()
                finish()
            }
            .setCancelable(false)
            .show()
    }

    override fun onResume() {
        super.onResume()
        actualizarInterfaz()
    }
}