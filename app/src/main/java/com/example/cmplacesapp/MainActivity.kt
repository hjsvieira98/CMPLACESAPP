package com.example.cmplacesapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import www.sanju.motiontoast.MotionToast

class MainActivity : AppCompatActivity() {
    private val sharedPrefFile = "UserLogado"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile,
            Context.MODE_PRIVATE)
        val sharedNameValue = sharedPreferences.getString("name_key","")

        if(sharedNameValue != ""){

            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("username", "admin")
            }
            startActivity(intent)
            finish()
        }else{

        }
        //Esconde actionbar do login
        supportActionBar?.hide()

        //Botão login
        val BtnL = findViewById<Button>(R.id.BtnLogin);
        BtnL.setOnClickListener({
            Login();
        })
    }

    fun Login (){
        val Username = findViewById<EditText>(R.id.username);
        val Password = findViewById<EditText>(R.id.username);
        if(Username.text.toString() == "Teste" && Password.text.toString() == "Teste" ){
            val intent2 = Intent(this, Dashboard::class.java).apply {

            }
            startActivity(intent2)
        }else{
            MotionToast.darkToast(this,"Profile Update Failed!","Teste",
                MotionToast.TOAST_ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this,R.font.helvetica_regular))

        }
    }


}

