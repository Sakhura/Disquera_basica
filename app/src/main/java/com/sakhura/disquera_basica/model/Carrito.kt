package com.sakhura.disquera_basica.model

import java.util.Date
// Representa un item en el carrito
data class ItemCarrito(
    val disco: Disco,
    var cantidad: Int,
    val fechaAgregado: Date = Date()
) {
    // Calcular subtotal del item
    fun getSubtotal(): Double {
        return disco.precio * cantidad
    }

    // Función para mostrar info del item
    fun getResumen(): String {
        return "${disco.nombre} x$cantidad = ${String.format("$%.2f", getSubtotal())}"
    }
}

// Gestor del carrito de compras
object GestorCarrito {

    // Lista de items en el carrito
    private val itemsCarrito = mutableListOf<ItemCarrito>()

    // Agregar disco al carrito
    fun agregarDisco(disco: Disco, cantidad: Int = 1): Boolean {

        if (cantidad <= 0) return false

        // Verificar si el disco ya está en el carrito
        val itemExistente = itemsCarrito.find { it.disco.id == disco.id }

        if (itemExistente != null) {
            // Si ya existe, aumentar la cantidad
            itemExistente.cantidad += cantidad
        } else {
            // Si no existe, crear nuevo item
            val nuevoItem = ItemCarrito(disco, cantidad)
            itemsCarrito.add(nuevoItem)
        }

        return true
    }

    // Remover disco del carrito
    fun removerDisco(discoId: Int) {
        itemsCarrito.removeAll { it.disco.id == discoId }
    }

    // Actualizar cantidad de un disco
    fun actualizarCantidad(discoId: Int, nuevaCantidad: Int): Boolean {

        if (nuevaCantidad <= 0) {
            removerDisco(discoId)
            return true
        }

        val item = itemsCarrito.find { it.disco.id == discoId }
        return if (item != null) {
            item.cantidad = nuevaCantidad
            true
        } else {
            false
        }
    }

    // Obtener todos los items del carrito
    fun getItems(): List<ItemCarrito> {
        return itemsCarrito.toList()
    }

    // Obtener cantidad total de items
    fun getCantidadTotalItems(): Int {
        return itemsCarrito.sumOf { it.cantidad }
    }

    // Calcular total del carrito
    fun getTotal(): Double {
        return itemsCarrito.sumOf { it.getSubtotal() }
    }

    // Verificar si el carrito está vacío
    fun estaVacio(): Boolean {
        return itemsCarrito.isEmpty()
    }

    // Vaciar el carrito
    fun vaciar() {
        itemsCarrito.clear()
    }

    // Obtener cantidad de un disco específico en el carrito
    fun getCantidadDisco(discoId: Int): Int {
        return itemsCarrito.find { it.disco.id == discoId }?.cantidad ?: 0
    }

    // Verificar si un disco está en el carrito
    fun contieneDisco(discoId: Int): Boolean {
        return itemsCarrito.any { it.disco.id == discoId }
    }

    // Obtener resumen del carrito para mostrar
    fun getResumenCarrito(): String {
        if (estaVacio()) {
            return "Carrito vacío"
        }

        val total = getTotal()
        val cantidadItems = getCantidadTotalItems()

        return "$cantidadItems items - ${String.format("$%.2f", total)}"
    }

    // Simular compra (por ahora solo vacía el carrito)
    fun realizarCompra(): ResultadoCompra {

        if (estaVacio()) {
            return ResultadoCompra.Error("El carrito está vacío")
        }

        // Aquí podrías agregar validaciones adicionales
        // como verificar stock, procesar pago, etc.

        val total = getTotal()
        val items = getItems()

        // Crear "pedido"
        val pedido = SimularPedido(
            id = "PED_${System.currentTimeMillis()}",
            items = items,
            total = total,
            fecha = Date()
        )

        // Vaciar carrito después de la compra
        vaciar()

        return ResultadoCompra.Exito("Compra realizada exitosamente", pedido)
    }
}

// Resultado de la compra
sealed class ResultadoCompra {
    data class Exito(val mensaje: String, val pedido: SimularPedido) : ResultadoCompra()
    data class Error(val mensaje: String) : ResultadoCompra()
}

// Simulación de un pedido
data class SimularPedido(
    val id: String,
    val items: List<ItemCarrito>,
    val total: Double,
    val fecha: Date
) {
    fun getResumen(): String {
        val cantidadItems = items.sumOf { it.cantidad }
        return "Pedido $id - $cantidadItems items - ${String.format("$%.2f", total)}"
    }
}