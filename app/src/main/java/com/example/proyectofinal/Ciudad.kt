package com.example.proyectofinal

class Ciudad {
    var id: Int = 0
    var nombre: String = ""

    constructor(nombre: String) {
        this.nombre = nombre
    }

    constructor()

    override fun toString(): String {
        return "ID: $id, Nombre: $nombre"
    }
}
