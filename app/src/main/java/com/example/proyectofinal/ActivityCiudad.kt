package com.example.proyectofinal

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectofinal.databinding.ActivityCiudadBinding

class ActivityCiudad : AppCompatActivity() {

    private lateinit var binding: ActivityCiudadBinding
    private lateinit var baseDeDatos: BaseDeDatos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Vinculamos el layout con ViewBinding
        binding = ActivityCiudadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializamos la base de datos
        baseDeDatos = BaseDeDatos(this)

        // Cargar y mostrar todas las ciudades al iniciar la actividad
        cargarCiudades()

        // Configurar botones
        configurarBotones()
    }

    private fun configurarBotones() {
        // Botón para adicionar una ciudad
        binding.btnAdicionarCiudad.setOnClickListener {
            val nombreCiudad = binding.etNombreCiudad.text.toString()

            if (nombreCiudad.isNotEmpty()) {
                val ciudad = Ciudad(nombreCiudad)
                val mensaje = baseDeDatos.insertarCiudad(ciudad)
                Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
                limpiarCampos()
                cargarCiudades() // Actualizar la lista de ciudades
            } else {
                Toast.makeText(this, "Por favor ingresa el nombre de la ciudad.", Toast.LENGTH_SHORT).show()
            }
        }

        // Botón para modificar una ciudad
        binding.btnModificarCiudad.setOnClickListener {
            val idCiudad = binding.etIdCiudad.text.toString().toIntOrNull()
            val nombreCiudad = binding.etNombreCiudad.text.toString()

            if (idCiudad != null && nombreCiudad.isNotEmpty()) {
                val mensaje = baseDeDatos.actualizarCiudad(idCiudad, nombreCiudad)
                Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
                limpiarCampos()
                cargarCiudades() // Actualizar la lista de ciudades
            } else {
                Toast.makeText(this, "Por favor completa el ID y el nombre de la ciudad.", Toast.LENGTH_SHORT).show()
            }
        }

        // Botón para borrar una ciudad
        binding.btnBorrarCiudad.setOnClickListener {
            val idCiudad = binding.etIdCiudad.text.toString().toIntOrNull()

            if (idCiudad != null) {
                val mensaje = baseDeDatos.borrarCiudad(idCiudad)
                Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
                limpiarCampos()
                cargarCiudades() // Actualizar la lista de ciudades
            } else {
                Toast.makeText(this, "Por favor ingresa el ID de la ciudad a borrar.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun cargarCiudades() {
        // Obtener la lista de ciudades desde la base de datos
        val listaCiudades = baseDeDatos.traerCiudades()

        // Mostrar la lista en el TextView
        if (listaCiudades.isNotEmpty()) {
            binding.txtInfoCiudad.text = listaCiudades.joinToString("\n") { "ID: ${it.id}, Nombre: ${it.nombre}" }
        } else {
            binding.txtInfoCiudad.text = "No hay ciudades registradas."
        }
    }

    private fun limpiarCampos() {
        binding.etIdCiudad.text.clear()
        binding.etNombreCiudad.text.clear()
    }
}
