package com.example.cmplacesapp.Mapas

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.example.cmplacesapp.Dashboard
import com.example.cmplacesapp.R
import com.example.cmplacesapp.retrofit.EndPoints
import com.example.cmplacesapp.retrofit.Incidentes
import com.example.cmplacesapp.retrofit.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import www.sanju.motiontoast.MotionToast
import java.io.ByteArrayOutputStream

class EditaMarker : AppCompatActivity() {
    private  val sharedPreferencesName: String = "AUTH"
    private var ID :String = "";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edita_marker)
        var Title =    intent.getStringExtra("Title")
        var Description =    intent.getStringExtra("Description")
        var Image =    intent.getStringExtra("Image")
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