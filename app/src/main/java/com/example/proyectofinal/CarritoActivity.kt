package com.example.proyectofinal

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.proyectofinal.databinding.ActivityCarritoBinding
import org.json.JSONObject

class CarritoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCarritoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Vincular el layout
        binding = ActivityCarritoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Cargar datos iniciales
        obtenerClientes()
        obtenerCiudades()
        obtenerArticulos()
        obtenerCarritos()

        // Configurar TextWatchers para subtotal dinámico
        configurarTextWatchers()

        // Configurar botones
        binding.btnAdicionarCarrito.setOnClickListener {
            val idArticulo = obtenerIdDeSpinner(binding.spArticuloCarrito.selectedItem.toString())
            val idCliente = obtenerIdDeSpinner(binding.spClienteCarrito.selectedItem.toString())
            val idCiudad = obtenerIdDeSpinner(binding.spCiudadCarrito.selectedItem.toString())
            val cantidad = binding.etCantidadCarrito.text.toString().toIntOrNull() ?: 0
            val valor = binding.etValorCarrito.text.toString().toDoubleOrNull() ?: 0.0
            val subtotal = cantidad * valor

            agregarCarrito(idArticulo, idCliente, idCiudad, cantidad, valor, subtotal)
        }

        binding.btnActualizarCarrito.setOnClickListener {
            val id = binding.etIdCarrito.text.toString().toIntOrNull()
            val idArticulo = obtenerIdDeSpinner(binding.spArticuloCarrito.selectedItem.toString())
            val idCliente = obtenerIdDeSpinner(binding.spClienteCarrito.selectedItem.toString())
            val idCiudad = obtenerIdDeSpinner(binding.spCiudadCarrito.selectedItem.toString())
            val cantidad = binding.etCantidadCarrito.text.toString().toIntOrNull() ?: 0
            val valor = binding.etValorCarrito.text.toString().toDoubleOrNull() ?: 0.0
            val subtotal = cantidad * valor

            if (id != null) {
                actualizarCarrito(id, idArticulo, idCliente, idCiudad, cantidad, valor, subtotal)
            } else {
                Toast.makeText(this, "Ingrese un ID válido", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnBorrarCarrito.setOnClickListener {
            val id = binding.etIdCarrito.text.toString().toIntOrNull()
            if (id != null) {
                borrarCarrito(id)
            } else {
                Toast.makeText(this, "Ingrese un ID válido", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun configurarTextWatchers() {
        binding.etCantidadCarrito.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                actualizarSubtotal()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.etValorCarrito.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                actualizarSubtotal()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun actualizarSubtotal() {
        val cantidad = binding.etCantidadCarrito.text.toString().toIntOrNull() ?: 0
        val valor = binding.etValorCarrito.text.toString().toDoubleOrNull() ?: 0.0
        val subtotal = cantidad * valor
        binding.txtViewSubtotalCarrito.text = subtotal.toString()
    }

    private fun obtenerClientes() {
        val url = "http://10.0.2.2/proyecto_final/cliente.php"
        val requestQueue = Volley.newRequestQueue(this)

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                val clientes = mutableListOf<String>()
                for (i in 0 until response.length()) {
                    val item = response.getJSONObject(i)
                    clientes.add("${item.getInt("id")}: ${item.getString("nombre")}")
                }
                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, clientes)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spClienteCarrito.adapter = adapter
            },
            { error ->
                Toast.makeText(this, "Error al obtener clientes: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )
        requestQueue.add(jsonArrayRequest)
    }

    private fun obtenerCiudades() {
        val url = "http://10.0.2.2/proyecto_final/ciudad.php"
        val requestQueue = Volley.newRequestQueue(this)

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                val ciudades = mutableListOf<String>()
                for (i in 0 until response.length()) {
                    val item = response.getJSONObject(i)
                    ciudades.add("${item.getInt("id")}: ${item.getString("nombre")}")
                }
                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ciudades)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spCiudadCarrito.adapter = adapter
            },
            { error ->
                Toast.makeText(this, "Error al obtener ciudades: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )
        requestQueue.add(jsonArrayRequest)
    }

    private fun obtenerArticulos() {
        val url = "http://10.0.2.2/proyecto_final/articulo.php"
        val requestQueue = Volley.newRequestQueue(this)

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                val articulos = mutableListOf<String>()
                for (i in 0 until response.length()) {
                    val item = response.getJSONObject(i)
                    articulos.add("${item.getInt("id")}: ${item.getString("nombre")}")
                }
                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, articulos)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spArticuloCarrito.adapter = adapter
            },
            { error ->
                Toast.makeText(this, "Error al obtener artículos: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )
        requestQueue.add(jsonArrayRequest)
    }

    private fun obtenerCarritos() {
        val url = "http://10.0.2.2/proyecto_final/carrito.php"
        val requestQueue = Volley.newRequestQueue(this)

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                val carritos = mutableListOf<String>()
                for (i in 0 until response.length()) {
                    val item = response.getJSONObject(i)
                    carritos.add("ID: ${item.getInt("id")}, Cliente: ${item.getInt("idCliente")}, " +
                            "Artículo: ${item.getInt("idArticulo")}, Cantidad: ${item.getInt("cantidad")}, " +
                            "Valor: ${item.getDouble("valor")}, Subtotal: ${item.getDouble("subtotal")}, " +
                            "Ciudad: ${item.getInt("idCiudad")}")
                }
                binding.txtInfoCarrito.text = if (carritos.isNotEmpty()) {
                    carritos.joinToString("\n")
                } else {
                    "No hay registros en el carrito."
                }
            },
            { error ->
                Toast.makeText(this, "Error al obtener carritos: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )
        requestQueue.add(jsonArrayRequest)
    }

    private fun agregarCarrito(
        idArticulo: Int,
        idCliente: Int,
        idCiudad: Int,
        cantidad: Int,
        valor: Double,
        subtotal: Double
    ) {
        val url = "http://10.0.2.2/proyecto_final/carrito.php"
        val requestQueue = Volley.newRequestQueue(this)

        val parametros = HashMap<String, String>()
        parametros["idArticulo"] = idArticulo.toString()
        parametros["idCliente"] = idCliente.toString()
        parametros["idCiudad"] = idCiudad.toString()
        parametros["cantidad"] = cantidad.toString()
        parametros["valor"] = valor.toString()
        parametros["subtotal"] = subtotal.toString()

        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            { response ->
                Toast.makeText(this, "Carrito agregado correctamente", Toast.LENGTH_SHORT).show()
                limpiarCampos()
                obtenerCarritos()
            },
            { error ->
                Toast.makeText(this, "Error al agregar carrito: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> = parametros
        }
        requestQueue.add(stringRequest)
    }

    private fun actualizarCarrito(
        id: Int,
        idArticulo: Int,
        idCliente: Int,
        idCiudad: Int,
        cantidad: Int,
        valor: Double,
        subtotal: Double
    ) {
        val url = "http://10.0.2.2/proyecto_final/modificarCarrito.php"
        val requestQueue = Volley.newRequestQueue(this)

        val parametros = HashMap<String, String>()
        parametros["id"] = id.toString()
        parametros["idArticulo"] = idArticulo.toString()
        parametros["idCliente"] = idCliente.toString()
        parametros["idCiudad"] = idCiudad.toString()
        parametros["cantidad"] = cantidad.toString()
        parametros["valor"] = valor.toString()
        parametros["subtotal"] = subtotal.toString()

        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            { response ->
                Toast.makeText(this, "Carrito actualizado correctamente", Toast.LENGTH_SHORT).show()
                limpiarCampos()
                obtenerCarritos()
            },
            { error ->
                Toast.makeText(this, "Error al actualizar carrito: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> = parametros
        }
        requestQueue.add(stringRequest)
    }

    private fun borrarCarrito(id: Int) {
        val url = "http://10.0.2.2/proyecto_final/borrarCarrito.php"
        val requestQueue = Volley.newRequestQueue(this)

        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            { response ->
                Toast.makeText(this, "Carrito eliminado correctamente", Toast.LENGTH_SHORT).show()
                limpiarCampos()
                obtenerCarritos()
            },
            { error ->
                Toast.makeText(this, "Error al borrar carrito: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                val parametros = HashMap<String, String>()
                parametros["id"] = id.toString()
                return parametros
            }
        }
        requestQueue.add(stringRequest)
    }

    private fun obtenerIdDeSpinner(item: String): Int {
        return item.split(":")[0].toIntOrNull() ?: 0
    }

    private fun limpiarCampos() {
        binding.etIdCarrito.text.clear()
        binding.etCantidadCarrito.text.clear()
        binding.etValorCarrito.text.clear()
    }
}