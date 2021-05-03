package com.example.cmplacesapp

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.cardview.widget.CardView
import com.example.cmplacesapp.Mapas.MapaActivity
import com.example.cmplacesapp.Notas.Notas

class Dashboard : AppCompatActivity() {
    private  val sharedPreferencesName: String = "AUTH"
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
        startActivity(Mapa, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
    }
    fun IniciaNotas (){
        val Notas = Intent(this, Notas::class.java).apply {

        }
        startActivity(Notas)
    }
    fun Logout(view: View){
        val sharedPref = getSharedPreferences(sharedPreferencesName,Context.MODE_PRIVATE) ?: return
        sharedPref.edit().remove(getString(R.string.Token)).apply()
        sharedPref.edit().remove("user_id").apply()
        val Login = Intent(this, MainActivity::class.java).apply {
        }
        startActivity(Login)

    }
}