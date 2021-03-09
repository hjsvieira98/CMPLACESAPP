package com.example.cmplacesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView
import com.example.cmplacesapp.Mapas.MapaActivity
import com.example.cmplacesapp.Notas.Notas

class Dashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
     val BtnMapa = findViewById<CardView>(R.id.Mapa)
     val BtnNotas = findViewById<CardView>(R.id.Notas)

        BtnMapa.setOnClickListener({IniciaMapa()});
        BtnNotas.setOnClickListener({IniciaNotas()});
    }
    fun IniciaMapa (){
        val Mapa = Intent(this, MapaActivity::class.java).apply {

        }
        startActivity(Mapa)
    }
    fun IniciaNotas (){
        val Notas = Intent(this, Notas::class.java).apply {

        }
        startActivity(Notas)
    }
}