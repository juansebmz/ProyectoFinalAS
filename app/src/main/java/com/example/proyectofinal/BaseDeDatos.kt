package com.example.proyectofinal

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.proyectofinal.Cliente

class BaseDeDatos(context: Context) : SQLiteOpenHelper(context, "CarritoDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val crearTablaArticulo = """
            CREATE TABLE Articulo (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                unidadMedida TEXT NOT NULL
            )
        """
        val crearTablaCliente = """
            CREATE TABLE Clientes (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                apellido TEXT NOT NULL,
                celular TEXT NOT NULL
            )
        """
        val crearTablaCiudad = """
            CREATE TABLE Ciudad (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL
            )
        """
        val crearTablaCarritoCompra = """
            CREATE TABLE CarritoCompra (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                idArticulo INTEGER NOT NULL,
                idCliente INTEGER NOT NULL,
                idCiudad INTEGER NOT NULL,
                cantidad INTEGER NOT NULL,
                valor REAL NOT NULL,
                subtotal REAL NOT NULL,
                FOREIGN KEY (idArticulo) REFERENCES Articulo (id),
                FOREIGN KEY (idCliente) REFERENCES Clientes (id),
                FOREIGN KEY (idCiudad) REFERENCES Ciudad (id)
            )
        """

        db?.execSQL(crearTablaArticulo)
        db?.execSQL(crearTablaCliente)
        db?.execSQL(crearTablaCiudad)
        db?.execSQL(crearTablaCarritoCompra)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS Articulo")
        db?.execSQL("DROP TABLE IF EXISTS Clientes")
        db?.execSQL("DROP TABLE IF EXISTS Ciudad")
        db?.execSQL("DROP TABLE IF EXISTS CarritoCompra")
        onCreate(db)
    }

    // Funciones para la tabla Articulo
    fun insertarArticulo(articulo: Articulo): String {
        val db = this.writableDatabase
        val contenedor = ContentValues()
        contenedor.put("nombre", articulo.nombre)
        contenedor.put("unidadMedida", articulo.unidadMedida)

        val resultado = db.insert("Articulo", null, contenedor)
        db.close()
        return if (resultado == -1L) "Error al insertar artículo" else "Artículo insertado correctamente"
    }

    fun traerArticulos(): MutableList<Articulo> {
        val lista: MutableList<Articulo> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT * FROM Articulo"
        val resultado = db.rawQuery(query, null)

        if (resultado.moveToFirst()) {
            do {
                val articulo = Articulo()
                articulo.id = resultado.getInt(resultado.getColumnIndexOrThrow("id"))
                articulo.nombre = resultado.getString(resultado.getColumnIndexOrThrow("nombre"))
                articulo.unidadMedida = resultado.getString(resultado.getColumnIndexOrThrow("unidadMedida"))
                lista.add(articulo)
            } while (resultado.moveToNext())
        }

        resultado.close()
        db.close()
        return lista
    }

    fun actualizarArticulo(id: Int, nombre: String, unidadMedida: String): String {
        val db = this.writableDatabase
        val contenedor = ContentValues()
        contenedor.put("nombre", nombre)
        contenedor.put("unidadMedida", unidadMedida)

        val resultado = db.update("Articulo", contenedor, "id=?", arrayOf(id.toString()))
        db.close()
        return if (resultado > 0) "Artículo actualizado correctamente" else "Error al actualizar artículo"
    }

    fun borrarArticulo(id: Int): String {
        val db = this.writableDatabase
        val resultado = db.delete("Articulo", "id=?", arrayOf(id.toString()))
        db.close()
        return if (resultado > 0) "Artículo eliminado correctamente" else "Error al eliminar artículo"
    }

    // Funciones para la tabla Clientes
    fun insertarCliente(cliente: Cliente): String {
        val db = this.writableDatabase
        val contenedor = ContentValues()
        contenedor.put("nombre", cliente.nombre)
        contenedor.put("apellido", cliente.apellido)
        contenedor.put("celular", cliente.celular)

        val resultado = db.insert("Clientes", null, contenedor)
        db.close()
        return if (resultado == -1L) "Error al insertar cliente" else "Cliente insertado correctamente"
    }

    fun traerClientes(): MutableList<Cliente> {
        val lista: MutableList<Cliente> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT * FROM Clientes"
        val resultado = db.rawQuery(query, null)

        if (resultado.moveToFirst()) {
            do {
                val cliente = Cliente()
                cliente.id = resultado.getInt(resultado.getColumnIndexOrThrow("id"))
                cliente.nombre = resultado.getString(resultado.getColumnIndexOrThrow("nombre"))
                cliente.apellido = resultado.getString(resultado.getColumnIndexOrThrow("apellido"))
                cliente.celular = resultado.getString(resultado.getColumnIndexOrThrow("celular"))
                lista.add(cliente)
            } while (resultado.moveToNext())
        }

        resultado.close()
        db.close()
        return lista
    }

    fun actualizarCliente(id: Int, nombre: String, apellido: String, celular: String): String {
        val db = this.writableDatabase
        val contenedor = ContentValues()
        contenedor.put("nombre", nombre)
        contenedor.put("apellido", apellido)
        contenedor.put("celular", celular)

        val resultado = db.update("Clientes", contenedor, "id=?", arrayOf(id.toString()))
        db.close()
        return if (resultado > 0) "Cliente actualizado correctamente" else "Error al actualizar cliente"
    }

    fun borrarCliente(id: Int): String {
        val db = this.writableDatabase
        val resultado = db.delete("Clientes", "id=?", arrayOf(id.toString()))
        db.close()
        return if (resultado > 0) "Cliente eliminado correctamente" else "Error al eliminar cliente"
    }

    // Funciones para la tabla Ciudad
    fun insertarCiudad(ciudad: Ciudad): String {
        val db = this.writableDatabase
        val contenedor = ContentValues()
        contenedor.put("nombre", ciudad.nombre)

        val resultado = db.insert("Ciudad", null, contenedor)
        db.close()
        return if (resultado == -1L) "Error al insertar ciudad" else "Ciudad insertada correctamente"
    }

    fun traerCiudades(): MutableList<Ciudad> {
        val lista: MutableList<Ciudad> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT * FROM Ciudad"
        val resultado = db.rawQuery(query, null)

        if (resultado.moveToFirst()) {
            do {
                val ciudad = Ciudad()
                ciudad.id = resultado.getInt(resultado.getColumnIndexOrThrow("id"))
                ciudad.nombre = resultado.getString(resultado.getColumnIndexOrThrow("nombre"))
                lista.add(ciudad)
            } while (resultado.moveToNext())
        }

        resultado.close()
        db.close()
        return lista
    }

    fun actualizarCiudad(id: Int, nombre: String): String {
        val db = this.writableDatabase
        val contenedor = ContentValues()
        contenedor.put("nombre", nombre)

        val resultado = db.update("Ciudad", contenedor, "id=?", arrayOf(id.toString()))
        db.close()
        return if (resultado > 0) "Ciudad actualizada correctamente" else "Error al actualizar ciudad"
    }

    fun borrarCiudad(id: Int): String {
        val db = this.writableDatabase
        val resultado = db.delete("Ciudad", "id=?", arrayOf(id.toString()))
        db.close()
        return if (resultado > 0) "Ciudad eliminada correctamente" else "Error al eliminar ciudad"
    }

    // Funciones para la tabla CarritoCompra
    fun insertarCarrito(carrito: CarritoCompra): String {
        val db = this.writableDatabase
        val contenedor = ContentValues()
        contenedor.put("idArticulo", carrito.idArticulo)
        contenedor.put("idCliente", carrito.idCliente)
        contenedor.put("idCiudad", carrito.idCiudad)
        contenedor.put("cantidad", carrito.cantidad)
        contenedor.put("valor", carrito.valor)
        contenedor.put("subtotal", carrito.subtotal)

        val resultado = db.insert("CarritoCompra", null, contenedor)
        db.close()
        return if (resultado == -1L) "Error al insertar carrito" else "Carrito insertado correctamente"
    }

    fun traerCarritos(): MutableList<CarritoCompra> {
        val lista: MutableList<CarritoCompra> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT * FROM CarritoCompra"
        val resultado = db.rawQuery(query, null)

        if (resultado.moveToFirst()) {
            do {
                val carrito = CarritoCompra()
                carrito.id = resultado.getInt(resultado.getColumnIndexOrThrow("id"))
                carrito.idArticulo = resultado.getInt(resultado.getColumnIndexOrThrow("idArticulo"))
                carrito.idCliente = resultado.getInt(resultado.getColumnIndexOrThrow("idCliente"))
                carrito.idCiudad = resultado.getInt(resultado.getColumnIndexOrThrow("idCiudad"))
                carrito.cantidad = resultado.getInt(resultado.getColumnIndexOrThrow("cantidad"))
                carrito.valor = resultado.getDouble(resultado.getColumnIndexOrThrow("valor"))
                carrito.subtotal = resultado.getDouble(resultado.getColumnIndexOrThrow("subtotal"))
                lista.add(carrito)
            } while (resultado.moveToNext())
        }

        resultado.close()
        db.close()
        return lista
    }

    fun actualizarCarrito(id: Int, idArticulo: Int, idCliente: Int, idCiudad: Int, cantidad: Int, valor: Double, subtotal: Double): String {
        val db = this.writableDatabase
        val contenedor = ContentValues()
        contenedor.put("idArticulo", idArticulo)
        contenedor.put("idCliente", idCliente)
        contenedor.put("idCiudad", idCiudad)
        contenedor.put("cantidad", cantidad)
        contenedor.put("valor", valor)
        contenedor.put("subtotal", subtotal)

        val resultado = db.update("CarritoCompra", contenedor, "id=?", arrayOf(id.toString()))
        db.close()
        return if (resultado > 0) "Carrito actualizado correctamente" else "Error al actualizar carrito"
    }

    fun borrarCarrito(id: Int): String {
        val db = this.writableDatabase
        val resultado = db.delete("CarritoCompra", "id=?", arrayOf(id.toString()))
        db.close()
        return if (resultado > 0) "Carrito eliminado correctamente" else "Error al eliminar carrito"
    }

}
