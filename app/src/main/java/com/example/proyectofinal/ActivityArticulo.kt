package com.example.proyectofinal

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.proyectofinal.databinding.ActivityArticuloBinding

class ActivityArticulo : AppCompatActivity() {

    private lateinit var binding: ActivityArticuloBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Vincular el layout
        binding = ActivityArticuloBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Cargar los artículos al iniciar la actividad
        obtenerArticulos()

        // Botón para agregar un artículo
        binding.btnAdicionarArticulo.setOnClickListener {
            val nombre = binding.etNombreArticulo.text.toString()
            val unidadMedida = binding.etUnidadMedidaArticulo.text.toString()

            if (nombre.isNotEmpty() && unidadMedida.isNotEmpty()) {
                agregarArticulo(nombre, unidadMedida)
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        // Botón para modificar un artículo
        binding.btnModificarArticulo.setOnClickListener {
            val id = binding.etIdArticulo.text.toString().toIntOrNull()
            val nombre = binding.etNombreArticulo.text.toString()
            val unidadMedida = binding.etUnidadMedidaArticulo.text.toString()

            if (id != null && nombre.isNotEmpty() && unidadMedida.isNotEmpty()) {
                modificarArticulo(id, nombre, unidadMedida)
            } else {
                Toast.makeText(this, "Complete todos los campos correctamente", Toast.LENGTH_SHORT).show()
            }
        }


        // Botón para borrar un artículo
        binding.btnBorrarArticulo.setOnClickListener {
            val id = binding.etIdArticulo.text.toString().toIntOrNull()
            if (id != null) {
                borrarArticulo(id)
            } else {
                Toast.makeText(this, "Por favor, ingrese un ID válido", Toast.LENGTH_SHORT).show()
            }
        }

    }

    // Función para obtener todos los artículos
    private fun obtenerArticulos() {
        val url = "http://10.0.2.2/proyecto_final/articulo.php"
        val requestQueue = Volley.newRequestQueue(this)

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                val articulos = mutableListOf<Articulo>()
                for (i in 0 until response.length()) {
                    val item = response.getJSONObject(i)
                    val articulo = Articulo(
                        item.getString("nombre"),
                        item.getString("unidadMedida")
                    )
                    articulo.id = item.getInt("id")
                    articulos.add(articulo)
                }
                mostrarArticulos(articulos)
            },
            { error ->
                Toast.makeText(this, "Error al obtener artículos: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )
        requestQueue.add(jsonArrayRequest)
    }

    // Función para agregar un artículo
    private fun agregarArticulo(nombre: String, unidadMedida: String) {
        val url = "http://10.0.2.2/proyecto_final/articulo.php"
        val requestQueue = Volley.newRequestQueue(this)

        val parametros = HashMap<String, String>()
        parametros["nombre"] = nombre
        parametros["unidadMedida"] = unidadMedida

        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            { response ->
                Toast.makeText(this, "Artículo agregado correctamente", Toast.LENGTH_SHORT).show()
                binding.etNombreArticulo.text.clear()
                binding.etUnidadMedidaArticulo.text.clear()
                obtenerArticulos()
            },
            { error ->
                Toast.makeText(this, "Error al agregar artículo: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> = parametros
        }
        requestQueue.add(stringRequest)
    }

    // Función para modificar un artículo
    private fun modificarArticulo(id: Int, nombre: String, unidadMedida: String) {
        val url = "http://10.0.2.2/proyecto_final/modificarArticulo.php"

        val request = object : StringRequest(
            Method.POST,
            url,
            Response.Listener { response ->
                // Si la respuesta es correcta
                Toast.makeText(this, "Artículo modificado correctamente", Toast.LENGTH_SHORT).show()
                obtenerArticulos() // Actualiza la lista de artículos
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                return hashMapOf(
                    "id" to id.toString(),
                    "nombre" to nombre,
                    "unidadMedida" to unidadMedida
                )
            }
        }

        Volley.newRequestQueue(this).add(request)
    }



    private fun borrarArticulo(id: Int) {
        val url = "http://10.0.2.2/proyecto_final/borrarArticulo.php"

        val request = object : StringRequest(
            Method.POST,
            url,
            Response.Listener { response ->
                // Si la respuesta es correcta
                Toast.makeText(this, "Artículo eliminado correctamente", Toast.LENGTH_SHORT).show()
                obtenerArticulos() // Actualiza la lista de artículos
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                return hashMapOf("id" to id.toString())
            }
        }

        Volley.newRequestQueue(this).add(request)
    }


    // Función para mostrar los artículos en el TextView
    private fun mostrarArticulos(articulos: List<Articulo>) {
        val texto = articulos.joinToString("\n") { "ID: ${it.id} - Nombre: ${it.nombre} - Unidad: ${it.unidadMedida}" }
        binding.txtInfoArticulo.text = texto
    }

}
