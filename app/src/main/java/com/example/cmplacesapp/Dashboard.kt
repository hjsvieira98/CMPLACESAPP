package com.example.cmplacesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView
import com.example.cmplacesapp.Mapas.MapaActivity

class Dashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
     val BtnMapa = findViewById<CardView>(R.id.Mapa)
        BtnMapa.setOnClickListener({IniciaMapa()});
    }
    fun IniciaMapa (){
        val Mapa = Intent(this, MapaActivity::class.java).apply {

        }
        startActivity(Mapa)
    }
}