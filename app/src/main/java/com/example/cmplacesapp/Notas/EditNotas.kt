package com.example.cmplacesapp.Notas

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.room.Room
import com.example.cmplacesapp.LocalDB.AppDatabase
import com.example.cmplacesapp.LocalDB.Notes
import com.example.cmplacesapp.R
import www.sanju.motiontoast.MotionToast
import java.text.SimpleDateFormat
import java.util.*

class EditNotas : AppCompatActivity() {
    private var note: Notes;
    init {
        this.note = Notes(null,"","","","")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_notas)
        val titulo = findViewById<EditText>(R.id.Titulo);
        val descricao = findViewById<EditText>(R.id.Descicao);
        this.note = Notes(
            intent.getStringExtra("Id")?.toInt(),intent.getStringExtra("Title"), intent.getStringExtra("Description"),
            intent.getStringExtra("DateAdd"),"")
        titulo.setText(note.Title)
        descricao.setText(note.Description)
    }

    fun saveNote(view: View){
        val titulo = findViewById<EditText>(R.id.Titulo);
        val descricao = findViewById<EditText>(R.id.Descicao);
        if(titulo.text.toString() == "" || descricao.text.toString() == ""){
            MotionToast.darkToast(this,"Atenção","Todos os campos necessitam de estar preenchidos",
                MotionToast.TOAST_WARNING,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this,R.font.helvetica_regular))
            return;
        }
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "DBLocal"
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        var nota = Notes( this.note.uid,titulo.text.toString(),descricao.text.toString(),
            this.note.DateAdd,currentDate)
        db.notesDao().Update(nota);
        MotionToast.darkToast(this,"Sucesso","Nota editada com sucesso",
            MotionToast.TOAST_SUCCESS,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(this,R.font.helvetica_regular))


        finish();

    }
}