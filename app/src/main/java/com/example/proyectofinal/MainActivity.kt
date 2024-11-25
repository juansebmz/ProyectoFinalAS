package com.example.proyectofinal

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectofinal.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Vinculamos el XML de MainActivity
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuramos el botón para abrir ActivityArticulo
        binding.btnAbrirArticulo.setOnClickListener {
            val intent = Intent(this, ActivityArticulo::class.java)
            startActivity(intent)
        }

        // Configuramos el botón para abrir ActivityCiudad
        binding.btnAbrirCiudad.setOnClickListener {
            val intent = Intent(this, ActivityCiudad::class.java)
            startActivity(intent)
        }

        // Configuramos el botón para abrir ClienteActivity
        binding.btnAbrirCliente.setOnClickListener {
            val intent = Intent(this, ClienteActivity::class.java)
            startActivity(intent)
        }

        binding.btnAbrirCarrito.setOnClickListener{
            val intent = Intent(this, CarritoActivity::class.java)
           startActivity(intent)
        }
    }
}
