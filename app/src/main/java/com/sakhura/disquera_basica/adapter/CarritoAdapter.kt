package com.sakhura.disquera_basica.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sakhura.disquera_basica.model.ItemCarrito
import com.sakhura.disquera_basica.R

class CarritoAdapter(
    private var items: List<ItemCarrito>,
    private val onCantidadChanged: (Int, Int) -> Unit, // discoId, nuevaCantidad
    private val onEliminarItem: (Int) -> Unit // discoId
) : RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder>() {

    class CarritoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombreDisco: TextView = view.findViewById(R.id.tv_nombre_disco_carrito)
        val tvArtista: TextView = view.findViewById(R.id.tv_artista_carrito)
        val tvPrecioUnitario: TextView = view.findViewById(R.id.tv_precio_unitario)
        val tvCantidad: TextView = view.findViewById(R.id.tv_cantidad)
        val tvSubtotal: TextView = view.findViewById(R.id.tv_subtotal)
        val btnMenos: Button = view.findViewById(R.id.btn_menos)
        val btnMas: Button = view.findViewById(R.id.btn_mas)
        val btnEliminar: Button = view.findViewById(R.id.btn_eliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_carrito, parent, false)
        return CarritoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {
        val item = items[position]
        val disco = item.disco

        // Mostrar información del disco
        holder.tvNombreDisco.text = disco.nombre
        holder.tvArtista.text = disco.artista
        holder.tvPrecioUnitario.text = disco.getPrecioFormateado()
        holder.tvCantidad.text = item.cantidad.toString()
        holder.tvSubtotal.text = String.format("$%.2f", item.getSubtotal())

        // Configurar botones de cantidad
        holder.btnMenos.setOnClickListener {
            val nuevaCantidad = item.cantidad - 1
            if (nuevaCantidad > 0) {
                onCantidadChanged(disco.id, nuevaCantidad)
            }
        }

        holder.btnMas.setOnClickListener {
            val nuevaCantidad = item.cantidad + 1
            onCantidadChanged(disco.id, nuevaCantidad)
        }

        // Configurar botón eliminar
        holder.btnEliminar.setOnClickListener {
            onEliminarItem(disco.id)
        }
    }

    override fun getItemCount(): Int = items.size

    // Actualizar la lista de items
    fun actualizarItems(nuevosItems: List<ItemCarrito>) {
        items = nuevosItems
        notifyDataSetChanged()
    }
}