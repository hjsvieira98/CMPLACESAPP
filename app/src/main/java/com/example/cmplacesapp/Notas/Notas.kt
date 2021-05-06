package com.example.cmplacesapp.Notas

import android.app.PendingIntent.getActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.cmplacesapp.LocalDB.AppDatabase
import com.example.cmplacesapp.LocalDB.Notes
import com.example.cmplacesapp.R
import com.example.cmplacesapp.RecycleAddapter.CustomAdapter
import www.sanju.motiontoast.MotionToast


class Notas : AppCompatActivity(),
    CustomAdapter.CustomAdapterListener {
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

            adapter = context?.let { CustomAdapter(db?.notesDao()?.getAll() as ArrayList<Notes>, this@Notas ,it) }
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

                db.notesDao().Delete(Nota)
                MotionToast.darkToast(this@Notas,"Sucesso","Nota apagada com sucesso",
                        MotionToast.TOAST_DELETE,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this@Notas,R.font.helvetica_regular))
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
            adapter = context?.let { CustomAdapter(db?.notesDao()?.getAll() as ArrayList<Notes>, this@Notas ,it) }

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

    override fun onNoteSelected(note: Notes?) {
        var id = note?.uid;
        var title = note?.Title;
        var Description = note?.Description
        var DateAdd = note?.DateAdd
        val intent = Intent(this, EditNotas::class.java)
        intent.putExtra("Id",id.toString());
        intent.putExtra("Title",title);
        intent.putExtra("Description",Description);
        intent.putExtra("DateAdd",DateAdd);
        startActivity(intent)

    }
}