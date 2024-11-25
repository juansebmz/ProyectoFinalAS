package com.example.proyectofinal

class CarritoCompra {
    var id: Int = 0
    var idArticulo: Int = 0
    var idCliente: Int = 0
    var cantidad: Int = 0
    var valor: Double = 0.0
    var subtotal: Double = 0.0
    var idCiudad: Int = 0

    constructor(idArticulo: Int, idCliente: Int, cantidad: Int, valor: Double, subtotal: Double, idCiudad: Int) {
        this.idArticulo = idArticulo
        this.idCliente = idCliente
        this.cantidad = cantidad
        this.valor = valor
        this.subtotal = subtotal
        this.idCiudad = idCiudad
    }

    constructor()
}
