package com.example.proyectofinal

class Cliente {
    var id: Int = 0
    var nombre: String = ""
    var apellido: String = ""
    var celular: String = ""

    constructor(nombre: String, apellido: String, celular: String) {
        this.nombre = nombre
        this.apellido = apellido
        this.celular = celular
    }

    constructor()
}
