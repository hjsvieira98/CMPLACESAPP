package com.example.cmplacesapp.Mapas

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import com.example.cmplacesapp.R
import com.example.cmplacesapp.retrofit.EndPoints
import com.example.cmplacesapp.retrofit.Incidentes
import com.example.cmplacesapp.retrofit.ServiceBuilder
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import www.sanju.motiontoast.MotionToast
import java.io.ByteArrayOutputStream

class NovoIncidente : AppCompatActivity() {
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private lateinit var bitmap: Bitmap
    private lateinit var incidentPicture: ImageView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private  val sharedPreferencesName: String = "AUTH"
    val Tipos = arrayOf<String>("Acidente", "Lixo", "Incendio")
    val REQUEST_CODE = 200
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_novo_incidente)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastKnownLocation()
        capturePhoto()
        incidentPicture = findViewById<ImageView>(R.id.imageView4)
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, Tipos)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        findViewById<Spinner>(R.id.spinner)!!.setAdapter(aa)
    }
    @SuppressLint("MissingPermission")
    fun getLastKnownLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            latitude = location.latitude
            longitude = location.longitude
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveIncident(view: View){
        val title = findViewById<EditText>(R.id.incidentTitle).text;
        val description = findViewById<EditText>(R.id.incidentDescription).text;
        if(title.toString() == "" || description.toString() == ""){
            MotionToast.darkToast(this,"Atenção","Todos os campos necessitam de estar preenchidos",
                MotionToast.TOAST_WARNING,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this,R.font.helvetica_regular))
            return;
        }
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
        val image:ByteArray = bos.toByteArray()
        val base64Encoded = java.util.Base64.getEncoder().encodeToString(image)
      val latitude = latitude
        val longitude = longitude
        val tipoIncidente = findViewById<Spinner>(R.id.spinner).selectedItem
        val sharedPref = getSharedPreferences(sharedPreferencesName,Context.MODE_PRIVATE) ?: return
        val userID = sharedPref.getString("user_id","0")
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.InsertIncidente(title.toString(),description.toString(),latitude.toString(),longitude.toString(),
            base64Encoded,userID?.toInt(),tipoIncidente.toString())

        call.enqueue(object : Callback<Incidentes> {
            override fun onResponse(call: Call<Incidentes>, response: Response<Incidentes>) {
                if (response.isSuccessful){
                    val c: Incidentes = response.body()!!
                    MotionToast.darkToast(this@NovoIncidente,"Sucesso","Marcador adicionado com sucesso",
                        MotionToast.TOAST_SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this@NovoIncidente,R.font.helvetica_regular))
                    finish()
                }else
                {
                    if (response.code() == 403 && response.message() == "login_fail") {
                        MotionToast.darkToast(this@NovoIncidente,"Erro","Problema de conexão ao servidor",
                            MotionToast.TOAST_ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(this@NovoIncidente,R.font.helvetica_regular))
                    } else {
                        MotionToast.darkToast(this@NovoIncidente,"Erro","Palavra pass ou utilizador errados",
                            MotionToast.TOAST_ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(this@NovoIncidente,R.font.helvetica_regular))
                    }
                }
            }

            override fun onFailure(call: Call<Incidentes>, t: Throwable) {
                Toast.makeText(this@NovoIncidente, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

    }


    fun capturePhoto() {

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (resultCode == Activity.RESULT_OK && data != null){
                bitmap = data?.extras?.get("data") as Bitmap
                incidentPicture.setImageBitmap(bitmap)
            }
            }

}