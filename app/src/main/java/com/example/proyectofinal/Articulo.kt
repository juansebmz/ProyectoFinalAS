package com.example.proyectofinal

class Articulo {
    var id: Int = 0
    var nombre: String = ""
    var unidadMedida: String = ""

    constructor(nombre: String, unidadMedida: String) {
        this.nombre = nombre
        this.unidadMedida = unidadMedida
    }

    constructor()
}
