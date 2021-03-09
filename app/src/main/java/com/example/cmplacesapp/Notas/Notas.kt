package com.example.cmplacesapp.Notas

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.cmplacesapp.LocalDB.AppDatabase
import com.example.cmplacesapp.LocalDB.Notes
import com.example.cmplacesapp.R
import com.example.cmplacesapp.RecycleAddapter.CustomAdapter


class Notas : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notas)
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "DBLocal"
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()

        val recycle = findViewById<RecyclerView>(R.id.Recyclerview)

        recycle.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = CustomAdapter(db?.notesDao()?.getAll() as ArrayList<Notes>)

            EmptyState();
            db.close();
        }
        swipeToDelete(recycle);
    }

    fun addNotas(view: View){
        val AddNotas = Intent(this, AddNotas()::class.java).apply {

        }
        startActivity(AddNotas)
    }
    private fun swipeToDelete(recyclerView: RecyclerView){

        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = recyclerView.adapter as CustomAdapter
                val db = Room.databaseBuilder(
                    applicationContext,
                    AppDatabase::class.java, "DBLocal"
                ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
                val Nota = adapter.removeAt(viewHolder.adapterPosition)
                println(Nota);
                db.notesDao().Delete(Nota)
                EmptyState();

            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onResume() {
        super.onResume()
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "DBLocal"
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()

        val recycle = findViewById<RecyclerView>(R.id.Recyclerview)
        recycle.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = CustomAdapter(db?.notesDao()?.getAll() as ArrayList<Notes>)
            EmptyState();
        }
        swipeToDelete(recycle);
    }
    fun EmptyState(){
        val recycle = findViewById<RecyclerView>(R.id.Recyclerview)
        val empty = findViewById<LinearLayout>(R.id.EmptyState)
        println(recycle.adapter!!.itemCount)
        if (recycle.adapter!!.itemCount == 0) {
            recycle.visibility = View.GONE;
            empty.visibility = View.VISIBLE;
        }
        else {
            recycle.visibility = View.VISIBLE;
            empty.visibility = View.GONE;
        }
    }
}