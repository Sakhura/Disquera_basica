package com.sakhura.disquera_basica.model

// Esta clase representa un disco de vinilo
// Es como una "plantilla" que define qué datos tiene cada disco
data class Disco(
    val id: Int,
    val nombre: String,
    val artista: String,
    val precio: Double,
    val genero: String,
    val año: Int,
    val imagen: String = "" // URL de la imagen (opcional por ahora)
) {
    // Función para mostrar el precio formateado
    fun getPrecioFormateado(): String {
        return "$${String.format("%.2f", precio)}"
    }

    // Función para mostrar la info completa
    fun getInfoCompleta(): String {
        return "$nombre - $artista ($año)"
    }
}

// Aquí creamos algunos discos de ejemplo para probar
// En una app real, estos datos vendrían de una base de datos o API
object DiscosEjemplo {

    fun obtenerDiscos(): List<Disco> {
        return listOf(
            Disco(
                id = 1,
                nombre = "Thriller",
                artista = "Michael Jackson",
                precio = 25.99,
                genero = "Pop",
                año = 1982
            ),
            Disco(
                id = 2,
                nombre = "Back in Black",
                artista = "AC/DC",
                precio = 22.50,
                genero = "Rock",
                año = 1980
            ),
            Disco(
                id = 3,
                nombre = "Abbey Road",
                artista = "The Beatles",
                precio = 28.00,
                genero = "Rock",
                año = 1969
            ),
            Disco(
                id = 4,
                nombre = "Dark Side of the Moon",
                artista = "Pink Floyd",
                precio = 24.99,
                genero = "Progressive Rock",
                año = 1973
            ),
            Disco(
                id = 5,
                nombre = "Nevermind",
                artista = "Nirvana",
                precio = 23.50,
                genero = "Grunge",
                año = 1991
            )
        )
    }

    // Función para buscar un disco por ID
    fun buscarPorId(id: Int): Disco? {
        return obtenerDiscos().find { it.id == id }
    }

    // Función para buscar discos por nombre o artista
    fun buscar(termino: String): List<Disco> {
        val terminoMinuscula = termino.lowercase()
        return obtenerDiscos().filter {
            it.nombre.lowercase().contains(terminoMinuscula) ||
                    it.artista.lowercase().contains(terminoMinuscula)
        }
    }
}