package com.example.cmplacesapp.Mapas

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.example.cmplacesapp.R
import com.example.cmplacesapp.retrofit.EndPoints
import com.example.cmplacesapp.retrofit.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import www.sanju.motiontoast.MotionToast

class EditaMarker : AppCompatActivity() {
    private  val sharedPreferencesName: String = "AUTH"
    private var ID :String = "";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edita_marker)
        val Tipos = arrayOf<String>("Acidente", "Lixo", "Incendio")
        var Title =    intent.getStringExtra("Title")
        var Description =    intent.getStringExtra("Description")
        var Image =    intent.getStringExtra("Image")
        var tipoIncidente =    intent.getStringExtra("tipoIncidente")
        var idUser =    intent.getStringExtra("user_id")
        ID  =    intent.getStringExtra("ID").toString()
        var editTextTitle = findViewById<EditText>(R.id.Title)
        var editTextDescription= findViewById<EditText>(R.id.Description)
        var editImage = findViewById<ImageView>(R.id.Imagem)
        editTextTitle.setText(Title)
        editTextDescription.setText(Description);
        val decodedString: ByteArray = Base64.decode(Image.toString(), Base64.DEFAULT)
        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        editImage.setImageBitmap(decodedByte)
        var btn = findViewById<Button>(R.id.BtnEditIncidente);
        btn.setOnClickListener {
            editMarker()
        }
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, Tipos)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        findViewById<Spinner>(R.id.spinner)!!.setAdapter(aa)
        findViewById<Spinner>(R.id.spinner).setSelection(Tipos.indexOf(tipoIncidente))
        val sharedPref = getSharedPreferences("AUTH", Context.MODE_PRIVATE) ?: return
        val user_id = sharedPref.getString("user_id", "0")
        if(user_id != idUser){
            val b = findViewById<Button>(R.id.BtnDelete)

            val b1 = findViewById<Button>(R.id.BtnEditIncidente)
            val b2 = findViewById<Spinner>(R.id.spinner)
            b.visibility = View.GONE
            b1.visibility = View.GONE
            b2.isEnabled = false
            editTextTitle.isEnabled = false;
            editTextDescription.isEnabled = false;

        }


    }

    fun editMarker(){

        val title = findViewById<EditText>(R.id.Title).text;
        val description = findViewById<EditText>(R.id.Description).text;
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.UpdateIncidente(ID?.toInt(),title.toString(),description.toString())
        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful){
                    val c: Int = response.body()!!
                    MotionToast.darkToast(this@EditaMarker,"Sucesso","Marcador editado com sucesso",
                            MotionToast.TOAST_SUCCESS,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(this@EditaMarker,R.font.helvetica_regular))
                    finish()

                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                Toast.makeText(this@EditaMarker, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

    }
    fun deleteMarker(view:View){
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.deleteIncidente(ID?.toInt())
        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful){
                    val c: Int = response.body()!!
                    if(c == 1){
                        MotionToast.darkToast(this@EditaMarker,"Sucesso","Marcador eliminado com sucesso",
                                MotionToast.TOAST_DELETE,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(this@EditaMarker,R.font.helvetica_regular))
                        finish()
                    }

                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                Toast.makeText(this@EditaMarker, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}