package com.example.cmplacesapp.Notas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.room.Room
import com.example.cmplacesapp.LocalDB.AppDatabase
import com.example.cmplacesapp.LocalDB.Notes
import com.example.cmplacesapp.R
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
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        val nota = Notes(null,titulo.text.toString(), descricao.text.toString(),currentDate,"","")
        db.notesDao().insertAll(nota);
        finish();
    }
}