package com.example.proyectofinal

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectofinal.databinding.ActivityArticuloBinding

class ActivityArticulo : AppCompatActivity() {

    private lateinit var binding: ActivityArticuloBinding
    private lateinit var baseDeDatos: BaseDeDatos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Vinculamos el XML de ActivityArticulo
        binding = ActivityArticuloBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializamos la base de datos
        baseDeDatos = BaseDeDatos(this)

        // Cargar los artículos registrados al iniciar la actividad
        cargarArticulos()

        // Configuramos el botón para agregar un artículo
        binding.btnAdicionarArticulo.setOnClickListener {
            val nombre = binding.etNombreArticulo.text.toString()
            val unidadMedida = binding.etUnidadMedidaArticulo.text.toString()

            if (nombre.isNotEmpty() && unidadMedida.isNotEmpty()) {
                val articulo = Articulo(nombre, unidadMedida)
                val mensaje = baseDeDatos.insertarArticulo(articulo)
                Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()

                // Limpiamos los campos de entrada
                binding.etNombreArticulo.text.clear()
                binding.etUnidadMedidaArticulo.text.clear()

                // Volver a cargar los artículos para mostrar el nuevo agregado
                cargarArticulos()
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnModificarArticulo.setOnClickListener {
            val id = binding.etIdArticulo.text.toString()
            val nombre = binding.etNombreArticulo.text.toString()
            val unidadMedida = binding.etUnidadMedidaArticulo.text.toString()

            if (id.isNotEmpty() && nombre.isNotEmpty() && unidadMedida.isNotEmpty()) {
                val idArticulo = id.toIntOrNull()
                if (idArticulo != null) {
                    val mensaje = baseDeDatos.actualizarArticulo(idArticulo, nombre, unidadMedida)
                    Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()

                    // Actualizar la lista de artículos
                    cargarArticulos()

                    // Limpiar los campos
                    binding.etIdArticulo.text.clear()
                    binding.etNombreArticulo.text.clear()
                    binding.etUnidadMedidaArticulo.text.clear()
                } else {
                    Toast.makeText(this, "El ID debe ser un número válido", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnBorrarArticulo.setOnClickListener {
            val id = binding.etIdArticulo.text.toString()

            if (id.isNotEmpty()) {
                val idArticulo = id.toIntOrNull()
                if (idArticulo != null) {
                    val mensaje = baseDeDatos.borrarArticulo(idArticulo)
                    Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()

                    // Actualizar la lista de artículos
                    cargarArticulos()

                    // Limpiar el campo de ID
                    binding.etIdArticulo.text.clear()
                } else {
                    Toast.makeText(this, "El ID debe ser un número válido", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, ingrese un ID", Toast.LENGTH_SHORT).show()
            }
        }


    }

    // Metodo para cargar y mostrar los artículos en el TextView
    private fun cargarArticulos() {
        val articulos = baseDeDatos.traerArticulos() // Este metodo debería devolver una lista de artículos

        // Mostrar la información de los artículos en el TextView
        val textoArticulos = articulos.joinToString("\n") { articulo ->
            "ID: ${articulo.id} - Nombre: ${articulo.nombre} - Unidad: ${articulo.unidadMedida}"
        }

        binding.txtInfoArticulo.text = textoArticulos
    }
}
