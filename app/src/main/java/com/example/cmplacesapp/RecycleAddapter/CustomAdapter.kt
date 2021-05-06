package com.example.cmplacesapp.RecycleAddapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cmplacesapp.LocalDB.Notes
import com.example.cmplacesapp.R

class CustomAdapter(dataSet: MutableList<Notes>, listener: CustomAdapterListener, context: Context) : RecyclerView.Adapter<CustomAdapter.ViewHolder>(){
    private val  listener: CustomAdapterListener
    private var dataSet: MutableList<Notes>;
    init{
        this.listener = listener
        this.dataSet = dataSet
    }
 inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        val DataAdd: TextView
        val Title: TextView
        val Desc: TextView

        init {
            // Define click listener for the ViewHolder's View.
            DataAdd = view.findViewById(R.id.TimeAdd)
            Title = view.findViewById(R.id.Title)
            Desc = view.findViewById(R.id.Desc)
            view.setOnClickListener {
                listener.onNoteSelected(dataSet[adapterPosition])
            }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.linerecycle, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.DataAdd.text = dataSet[position].DateAdd;
        viewHolder.Title.text = dataSet[position].Title;
        viewHolder.Desc.text = dataSet[position].Description;
    }
    fun removeAt(position: Int):Notes {
        val note = dataSet[position]
        dataSet!!.removeAt(position)
        notifyItemRemoved(position)
        return note
     }


    interface CustomAdapterListener {
        fun onNoteSelected(note: Notes?)
    }
    fun getCount():Int{
      return  dataSet.size;
    }

    override fun getItemCount() = dataSet.size

}
