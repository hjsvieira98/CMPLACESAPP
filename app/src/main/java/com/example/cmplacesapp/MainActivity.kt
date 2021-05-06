package com.example.cmplacesapp

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.example.cmplacesapp.Notas.Notas
import com.example.cmplacesapp.retrofit.EndPoints
import com.example.cmplacesapp.retrofit.ServiceBuilder
import com.example.cmplacesapp.retrofit.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import www.sanju.motiontoast.MotionToast

class MainActivity : AppCompatActivity() {
    private  val sharedPreferencesName: String = "AUTH"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        val sharedPref = getSharedPreferences(sharedPreferencesName,Context.MODE_PRIVATE) ?: return

        val Token = sharedPref.getString(getString(R.string.Token),"")
        if(Token != ""){
            val EntraDashBoard = Intent(this@MainActivity, Dashboard::class.java).apply {
            }
            startActivity(EntraDashBoard)
            finish();
        }

        supportActionBar?.hide()

    }
    fun EntraNostas (view:View){
         val EntraNotas = Intent(this, Notas::class.java).apply {
            }

            startActivity(EntraNotas)
    }

    fun Login(view: View) {
        val Username = findViewById<EditText>(R.id.username);
        val Password = findViewById<EditText>(R.id.password);
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.Login(Username.text.toString(),Password.text.toString())

        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful){
                    val c: User = response.body()!!
                    if(c.Token !=null){
                         with (getSharedPreferences(sharedPreferencesName,Context.MODE_PRIVATE).edit()) {
                            putString(getString(R.string.Token), c.Token)
                            putString("user_id", c.id.toString())
                            commit()
                        }
                        MotionToast.darkToast(this@MainActivity,"Sucesso","Login efetuado com sucesso",
                            MotionToast.TOAST_SUCCESS,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(this@MainActivity,R.font.helvetica_regular))
                        val EntraDashBoard = Intent(this@MainActivity, Dashboard::class.java).apply {
                        }

                        startActivity(EntraDashBoard)
                        finish()
                    }else{
                        if (response.code() == 403 && response.message() == "login_fail") {
                            MotionToast.darkToast(this@MainActivity,"Erro","Problema de conexão ao servidor",
                                MotionToast.TOAST_ERROR,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(this@MainActivity,R.font.helvetica_regular))
                        } else {
                            MotionToast.darkToast(this@MainActivity,"Erro","Palavra pass ou utilizador errados",
                                MotionToast.TOAST_ERROR,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(this@MainActivity,R.font.helvetica_regular))
                        }
                    }

                }else
                {
                    if (response.code() == 403 && response.message() == "login_fail") {
                        MotionToast.darkToast(this@MainActivity,"Erro","Problema de conexão ao servidor",
                                MotionToast.TOAST_ERROR,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(this@MainActivity,R.font.helvetica_regular))
                    } else {
                        MotionToast.darkToast(this@MainActivity,"Erro","Palavra pass ou utilizador errados",
                                MotionToast.TOAST_ERROR,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(this@MainActivity,R.font.helvetica_regular))
                    }
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


}

