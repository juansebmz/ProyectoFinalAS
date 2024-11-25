package com.example.proyectofinal

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.proyectofinal.databinding.ActivityCarritoBinding

class CarritoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCarritoBinding
    private lateinit var db: BaseDeDatos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarritoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = BaseDeDatos(this)

        // Cargar datos en los Spinners
        cargarClientes()
        cargarCiudades()
        cargarArticulos()

        // Mostrar el carrito inicial
        cargarCarrito()

        // Configurar los TextWatchers para actualizar el subtotal dinámicamente
        configurarTextWatchers()

        // Configurar botones
        binding.btnAdicionarCarrito.setOnClickListener {
            adicionarCarrito()
            cargarCarrito() // Actualizar lista
        }
        binding.btnActualizarCarrito.setOnClickListener {
            actualizarCarrito()
            cargarCarrito() // Actualizar lista
        }
        binding.btnBorrarCarrito.setOnClickListener {
            borrarCarrito()
            cargarCarrito() // Actualizar lista
        }
    }

    private fun configurarTextWatchers() {
        // Configurar el TextWatcher para el campo de cantidad
        binding.etCantidadCarrito.addTextChangedListener {
            actualizarSubtotal()
        }
        // Configurar el TextWatcher para el campo de valor
        binding.etValorCarrito.addTextChangedListener {
            actualizarSubtotal()
        }
    }

    private fun actualizarSubtotal() {
        val cantidad = binding.etCantidadCarrito.text.toString().toIntOrNull() ?: 0
        val valor = binding.etValorCarrito.text.toString().toDoubleOrNull() ?: 0.0
        val subtotal = cantidad * valor
        // Actualizar el campo de subtotal
        binding.txtViewSubtotalCarrito.setText(subtotal.toString())
    }

    private fun cargarClientes() {
        val clientes = db.traerClientes()
        val nombresClientes = clientes.map { "${it.id}: ${it.nombre}" }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nombresClientes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spClienteCarrito.adapter = adapter
    }

    private fun cargarCiudades() {
        val ciudades = db.traerCiudades()
        val nombresCiudades = ciudades.map { "${it.id}: ${it.nombre}" }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nombresCiudades)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spCiudadCarrito.adapter = adapter
    }

    private fun cargarArticulos() {
        val articulos = db.traerArticulos()
        val nombresArticulos = articulos.map { "${it.id}: ${it.nombre}" }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nombresArticulos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spArticuloCarrito.adapter = adapter
    }

    private fun cargarCarrito() {
        val carritos = db.traerCarritos()
        if (carritos.isNotEmpty()) {
            binding.txtInfoCarrito.text = carritos.joinToString("\n") {
                "ID: ${it.id}, Cliente: ${it.idCliente}, Artículo: ${it.idArticulo}, Cantidad: ${it.cantidad}, Valor: ${it.valor}, Subtotal: ${it.subtotal}, Ciudad: ${it.idCiudad}"
            }
        } else {
            binding.txtInfoCarrito.text = "No hay registros en el carrito."
        }
    }

    private fun adicionarCarrito() {
        val idArticulo = obtenerIdDeSpinner(binding.spArticuloCarrito.selectedItem.toString())
        val idCliente = obtenerIdDeSpinner(binding.spClienteCarrito.selectedItem.toString())
        val idCiudad = obtenerIdDeSpinner(binding.spCiudadCarrito.selectedItem.toString())
        val cantidad = binding.etCantidadCarrito.text.toString().toIntOrNull() ?: 0
        val valor = binding.etValorCarrito.text.toString().toDoubleOrNull() ?: 0.0
        val subtotal = cantidad * valor

        val carrito = CarritoCompra(idArticulo, idCliente, cantidad, valor, subtotal, idCiudad)
        val mensaje = db.insertarCarrito(carrito)
        mostrarMensaje(mensaje)
        limpiarCampos()
    }

    private fun actualizarCarrito() {
        val id = binding.etIdCarrito.text.toString().toIntOrNull() ?: return
        val idArticulo = obtenerIdDeSpinner(binding.spArticuloCarrito.selectedItem.toString())
        val idCliente = obtenerIdDeSpinner(binding.spClienteCarrito.selectedItem.toString())
        val idCiudad = obtenerIdDeSpinner(binding.spCiudadCarrito.selectedItem.toString())
        val cantidad = binding.etCantidadCarrito.text.toString().toIntOrNull() ?: 0
        val valor = binding.etValorCarrito.text.toString().toDoubleOrNull() ?: 0.0
        val subtotal = cantidad * valor

        val mensaje = db.actualizarCarrito(id, idArticulo, idCliente, idCiudad, cantidad, valor, subtotal)
        mostrarMensaje(mensaje)
        limpiarCampos()
    }

    private fun borrarCarrito() {
        val id = binding.etIdCarrito.text.toString().toIntOrNull() ?: return
        val mensaje = db.borrarCarrito(id)
        mostrarMensaje(mensaje)
        limpiarCampos()
    }

    private fun obtenerIdDeSpinner(item: String): Int {
        return item.split(":")[0].toIntOrNull() ?: 0
    }

    private fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    private fun limpiarCampos() {
        binding.etIdCarrito.text.clear()
        binding.etCantidadCarrito.text.clear()
        binding.etValorCarrito.text.clear()
    }
}
