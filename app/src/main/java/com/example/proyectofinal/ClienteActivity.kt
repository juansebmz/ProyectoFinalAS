package com.example.proyectofinal

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectofinal.databinding.ActivityClienteBinding

class ClienteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClienteBinding
    private lateinit var db: BaseDeDatos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = BaseDeDatos(this)

        // Cargar y mostrar los clientes al iniciar
        cargarClientes()

        // Botón Adicionar Cliente
        binding.btnAdicionarCliente.setOnClickListener {
            val nombre = binding.etNombreCliente.text.toString()
            val apellido = binding.etApellidoCliente.text.toString()
            val celular = binding.etCelularCliente.text.toString()

            if (nombre.isNotEmpty() && apellido.isNotEmpty() && celular.isNotEmpty()) {
                val cliente = Cliente(nombre, apellido, celular)
                val resultado = db.insertarCliente(cliente)
                mostrarMensaje(resultado)
                limpiarCampos()
                cargarClientes() // Actualizar la lista de clientes
            } else {
                mostrarMensaje("Todos los campos son obligatorios")
            }
        }

        // Botón Actualizar Cliente
        binding.btnActualizarCliente.setOnClickListener {
            val id = binding.etIdCliente.text.toString().toIntOrNull()
            val nombre = binding.etNombreCliente.text.toString()
            val apellido = binding.etApellidoCliente.text.toString()
            val celular = binding.etCelularCliente.text.toString()

            if (id != null && nombre.isNotEmpty() && apellido.isNotEmpty() && celular.isNotEmpty()) {
                val resultado = db.actualizarCliente(id, nombre, apellido, celular)
                mostrarMensaje(resultado)
                limpiarCampos()
                cargarClientes() // Actualizar la lista de clientes
            } else {
                mostrarMensaje("Todos los campos son obligatorios")
            }
        }

        // Botón Borrar Cliente
        binding.btnBorrarCliente.setOnClickListener {
            val id = binding.etIdCliente.text.toString().toIntOrNull()

            if (id != null) {
                val resultado = db.borrarCliente(id)
                mostrarMensaje(resultado)
                limpiarCampos()
                cargarClientes() // Actualizar la lista de clientes
            } else {
                mostrarMensaje("Debes ingresar un ID válido")
            }
        }
    }

    private fun cargarClientes() {
        // Obtener la lista de clientes desde la base de datos
        val listaClientes = db.traerClientes()

        // Mostrar la lista en el TextView
        if (listaClientes.isNotEmpty()) {
            binding.txtInfoCliente.text = listaClientes.joinToString("\n") {
                "ID: ${it.id}, Nombre: ${it.nombre}, Apellido: ${it.apellido}, Celular: ${it.celular}"
            }
        } else {
            binding.txtInfoCliente.text = "No hay clientes registrados."
        }
    }

    private fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    private fun limpiarCampos() {
        binding.etIdCliente.text.clear()
        binding.etNombreCliente.text.clear()
        binding.etApellidoCliente.text.clear()
        binding.etCelularCliente.text.clear()
    }
}
