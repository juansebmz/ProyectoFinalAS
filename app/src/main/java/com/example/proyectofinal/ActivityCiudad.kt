package com.example.proyectofinal

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.proyectofinal.databinding.ActivityCiudadBinding

class ActivityCiudad : AppCompatActivity() {

    private lateinit var binding: ActivityCiudadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Vincular el layout
        binding = ActivityCiudadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Cargar las ciudades al iniciar la actividad
        obtenerCiudades()

        // Botón para agregar una ciudad
        binding.btnAdicionarCiudad.setOnClickListener {
            val nombre = binding.etNombreCiudad.text.toString()

            if (nombre.isNotEmpty()) {
                agregarCiudad(nombre)
            } else {
                Toast.makeText(this, "Por favor, ingrese el nombre de la ciudad", Toast.LENGTH_SHORT).show()
            }
        }

        // Botón para modificar una ciudad
        binding.btnModificarCiudad.setOnClickListener {
            val id = binding.etIdCiudad.text.toString().toIntOrNull()
            val nombre = binding.etNombreCiudad.text.toString()

            if (id != null && nombre.isNotEmpty()) {
                modificarCiudad(id, nombre)
            } else {
                Toast.makeText(this, "Complete todos los campos correctamente", Toast.LENGTH_SHORT).show()
            }
        }

        // Botón para borrar una ciudad
        binding.btnBorrarCiudad.setOnClickListener {
            val id = binding.etIdCiudad.text.toString().toIntOrNull()
            if (id != null) {
                borrarCiudad(id)
            } else {
                Toast.makeText(this, "Por favor, ingrese un ID válido", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun obtenerCiudades() {
        val url = "http://10.0.2.2/proyecto_final/ciudad.php"
        val requestQueue = Volley.newRequestQueue(this)

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                val ciudades = mutableListOf<Ciudad>()
                for (i in 0 until response.length()) {
                    val item = response.getJSONObject(i)
                    val ciudad = Ciudad(item.getString("nombre"))
                    ciudad.id = item.getInt("id")
                    ciudades.add(ciudad)
                }
                mostrarCiudades(ciudades)
            },
            { error ->
                Toast.makeText(this, "Error al obtener ciudades: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )
        requestQueue.add(jsonArrayRequest)
    }

    private fun agregarCiudad(nombre: String) {
        val url = "http://10.0.2.2/proyecto_final/ciudad.php"
        val requestQueue = Volley.newRequestQueue(this)

        val parametros = HashMap<String, String>()
        parametros["nombre"] = nombre

        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            { response ->
                Toast.makeText(this, "Ciudad agregada correctamente", Toast.LENGTH_SHORT).show()
                binding.etNombreCiudad.text.clear()
                obtenerCiudades()
            },
            { error ->
                Toast.makeText(this, "Error al agregar ciudad: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> = parametros
        }
        requestQueue.add(stringRequest)
    }

    private fun modificarCiudad(id: Int, nombre: String) {
        val url = "http://10.0.2.2/proyecto_final/modificarCiudad.php"

        val request = object : StringRequest(
            Method.POST,
            url,
            Response.Listener {
                Toast.makeText(this, "Ciudad modificada correctamente", Toast.LENGTH_SHORT).show()
                obtenerCiudades()
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                return hashMapOf(
                    "id" to id.toString(),
                    "nombre" to nombre
                )
            }
        }

        Volley.newRequestQueue(this).add(request)
    }

    private fun borrarCiudad(id: Int) {
        val url = "http://10.0.2.2/proyecto_final/borrarCiudad.php"

        val request = object : StringRequest(
            Method.POST,
            url,
            Response.Listener {
                Toast.makeText(this, "Ciudad eliminada correctamente", Toast.LENGTH_SHORT).show()
                obtenerCiudades()
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

    private fun mostrarCiudades(ciudades: List<Ciudad>) {
        val texto = ciudades.joinToString("\n") { "ID: ${it.id} - Nombre: ${it.nombre}" }
        binding.txtInfoCiudad.text = texto
    }
}

