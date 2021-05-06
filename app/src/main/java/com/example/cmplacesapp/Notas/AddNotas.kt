package com.example.cmplacesapp.Notas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.content.res.ResourcesCompat
import androidx.room.Room
import com.example.cmplacesapp.LocalDB.AppDatabase
import com.example.cmplacesapp.LocalDB.Notes
import com.example.cmplacesapp.R
import www.sanju.motiontoast.MotionToast
import java.text.SimpleDateFormat
import java.util.*

class AddNotas : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notas)
        setTitle("Notas");
    }

    fun guardaNota(view: View){
        val titulo = findViewById<EditText>(R.id.Titulo);
        val descricao = findViewById<EditText>(R.id.Descicao);
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "DBLocal"
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        if(titulo.text.toString() == "" || descricao.text.toString() == ""){
            MotionToast.darkToast(this,"Atenção","Todos os campos necessitam de estar preenchidos",
                MotionToast.TOAST_WARNING,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this,R.font.helvetica_regular))
            return;
        }
        val nota = Notes(null,titulo.text.toString(), descricao.text.toString(),currentDate,"")
        var result = db.notesDao().insertAll(nota);
        MotionToast.darkToast(this,"Sucesso","Nota criada com sucesso",
                    MotionToast.TOAST_SUCCESS,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this,R.font.helvetica_regular))


        finish();
    }
}