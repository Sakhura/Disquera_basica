package com.sakhura.disquera_basica.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sakhura.disquera_basica.R
import com.sakhura.disquera_basica.model.Disco

// Este adaptador conecta los datos (lista de discos) con la vista (RecyclerView)
// Piénsalo como un "traductor" entre tus datos y lo que ve el usuario
class DiscoAdapter(
    private var discos: List<Disco>,
    private val onDiscoClick: (Disco) -> Unit // Función que se ejecuta al tocar un disco
) : RecyclerView.Adapter<DiscoAdapter.DiscoViewHolder>() {

    // ViewHolder: representa cada item individual en la lista
    class DiscoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombreTextView: TextView = view.findViewById(R.id.tv_nombre_disco)
        val artistaTextView: TextView = view.findViewById(R.id.tv_artista_disco)
        val precioTextView: TextView = view.findViewById(R.id.tv_precio_disco)
        val generoTextView: TextView = view.findViewById(R.id.tv_genero_disco)
    }

    // Este método se llama cuando se necesita crear un nuevo ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscoViewHolder {
        // Inflamos el layout para cada item de la lista
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_disco, parent, false)
        return DiscoViewHolder(view)
    }

    // Este método asigna los datos a cada ViewHolder
    override fun onBindViewHolder(holder: DiscoViewHolder, position: Int) {
        val disco = discos[position]

        // Asignamos los datos a las vistas
        holder.nombreTextView.text = disco.nombre
        holder.artistaTextView.text = disco.artista
        holder.precioTextView.text = disco.getPrecioFormateado()
        holder.generoTextView.text = disco.genero

        // Configuramos el click en el item
        holder.itemView.setOnClickListener {
            onDiscoClick(disco) // Ejecutamos la función que nos pasaron
        }
    }

    // Devuelve el número total de items
    override fun getItemCount(): Int = discos.size

    // Función para actualizar la lista de discos (útil para búsquedas y filtros)
    fun actualizarDiscos(nuevosDiscos: List<Disco>) {
        discos = nuevosDiscos
        notifyDataSetChanged() // Le dice al RecyclerView que se actualice
    }
}