package com.example.cmplacesapp

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.cmplacesapp.Mapas.MapaActivity
import com.example.cmplacesapp.Notas.Notas
import com.google.android.gms.maps.model.MapStyleOptions

class Dashboard : AppCompatActivity(), SensorEventListener {
    private  val sharedPreferencesName: String = "AUTH"
    private lateinit var sensorManager: SensorManager
    private var temperature: Sensor? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
     val BtnMapa = findViewById<CardView>(R.id.Mapa)
     val BtnNotas = findViewById<CardView>(R.id.Notas)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        temperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
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
    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Do something here if sensor accuracy changes.
    }

    @SuppressLint("ResourceAsColor")
    override fun onSensorChanged(event: SensorEvent) {
        if(event.values[0] > 0 && event.values[0] <=30)
            findViewById<TextView>(R.id.Temperature).setTextColor(R.color.good)
        if(event.values[0] > 30)
            findViewById<TextView>(R.id.Temperature).setTextColor(R.color.hot)
        if(event.values[0] < 0)
            findViewById<TextView>(R.id.Temperature).setTextColor(R.color.cold)

        findViewById<TextView>(R.id.Temperature).text = event.values[0].toString() + "Cº "
    }


    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
    override fun onResume() {
        // Register a listener for the sensor.
        super.onResume()
        sensorManager.registerListener(this, temperature, SensorManager.SENSOR_DELAY_NORMAL)
    }
}