package com.example.proyectofinal

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.proyectofinal.databinding.ActivityClienteBinding

class ClienteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClienteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Vincular el layout
        binding = ActivityClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Cargar los clientes al iniciar la actividad
        obtenerClientes()

        // Botón para agregar un cliente
        binding.btnAdicionarCliente.setOnClickListener {
            val nombre = binding.etNombreCliente.text.toString()
            val apellido = binding.etApellidoCliente.text.toString()
            val celular = binding.etCelularCliente.text.toString()

            if (nombre.isNotEmpty() && apellido.isNotEmpty() && celular.isNotEmpty()) {
                agregarCliente(nombre, apellido, celular)
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        // Botón para modificar un cliente
        binding.btnActualizarCliente.setOnClickListener {
            val id = binding.etIdCliente.text.toString().toIntOrNull()
            val nombre = binding.etNombreCliente.text.toString()
            val apellido = binding.etApellidoCliente.text.toString()
            val celular = binding.etCelularCliente.text.toString()

            if (id != null && nombre.isNotEmpty() && apellido.isNotEmpty() && celular.isNotEmpty()) {
                modificarCliente(id, nombre, apellido, celular)
            } else {
                Toast.makeText(this, "Complete todos los campos correctamente", Toast.LENGTH_SHORT).show()
            }
        }

        // Botón para borrar un cliente
        binding.btnBorrarCliente.setOnClickListener {
            val id = binding.etIdCliente.text.toString().toIntOrNull()
            if (id != null) {
                borrarCliente(id)
            } else {
                Toast.makeText(this, "Por favor, ingrese un ID válido", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Función para obtener todos los clientes
    private fun obtenerClientes() {
        val url = "http://10.0.2.2/proyecto_final/cliente.php"
        val requestQueue = Volley.newRequestQueue(this)

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                val clientes = mutableListOf<Cliente>()
                for (i in 0 until response.length()) {
                    val item = response.getJSONObject(i)
                    val cliente = Cliente(
                        item.getString("nombre"),
                        item.getString("apellido"),
                        item.getString("celular")
                    )
                    cliente.id = item.getInt("id")
                    clientes.add(cliente)
                }
                mostrarClientes(clientes)
            },
            { error ->
                Toast.makeText(this, "Error al obtener clientes: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )
        requestQueue.add(jsonArrayRequest)
    }

    // Función para agregar un cliente
    private fun agregarCliente(nombre: String, apellido: String, celular: String) {
        val url = "http://10.0.2.2/proyecto_final/cliente.php"
        val requestQueue = Volley.newRequestQueue(this)

        val parametros = HashMap<String, String>()
        parametros["nombre"] = nombre
        parametros["apellido"] = apellido
        parametros["celular"] = celular

        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            { response ->
                Toast.makeText(this, "Cliente agregado correctamente", Toast.LENGTH_SHORT).show()
                binding.etNombreCliente.text.clear()
                binding.etApellidoCliente.text.clear()
                binding.etCelularCliente.text.clear()
                obtenerClientes()
            },
            { error ->
                Toast.makeText(this, "Error al agregar cliente: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> = parametros
        }
        requestQueue.add(stringRequest)
    }

    // Función para modificar un cliente
    private fun modificarCliente(id: Int, nombre: String, apellido: String, celular: String) {
        val url = "http://10.0.2.2/proyecto_final/modificarCliente.php"

        val request = object : StringRequest(
            Method.POST,
            url,
            Response.Listener { response ->
                Toast.makeText(this, "Cliente modificado correctamente", Toast.LENGTH_SHORT).show()
                obtenerClientes() // Actualiza la lista de clientes
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
                    "apellido" to apellido,
                    "celular" to celular
                )
            }
        }

        Volley.newRequestQueue(this).add(request)
    }

    // Función para borrar un cliente
    private fun borrarCliente(id: Int) {
        val url = "http://10.0.2.2/proyecto_final/borrarCliente.php"

        val request = object : StringRequest(
            Method.POST,
            url,
            Response.Listener { response ->
                Toast.makeText(this, "Cliente eliminado correctamente", Toast.LENGTH_SHORT).show()
                obtenerClientes() // Actualiza la lista de clientes
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

    // Función para mostrar los clientes en el TextView
    private fun mostrarClientes(clientes: List<Cliente>) {
        val texto = clientes.joinToString("\n") { "ID: ${it.id} - Nombre: ${it.nombre} ${it.apellido} - Celular: ${it.celular}" }
        binding.txtInfoCliente.text = texto
    }
}
