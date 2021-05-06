package com.example.cmplacesapp.Mapas

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.FragmentManager
import com.example.cmplacesapp.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class FilterFragment : BottomSheetDialogFragment() {

    companion object {
        private const val TAG = "Filter"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (savedInstanceState != null) dismiss()
        val view = inflater.inflate(R.layout.filter_fragment, container, false)
        val preferences: SharedPreferences = context!!.getSharedPreferences("FILTERMAP", Context.MODE_PRIVATE)
        view.findViewById<CheckBox>(R.id.checkbox_1).isChecked = preferences.getBoolean("0", true)
        view.findViewById<CheckBox>(R.id.checkbox_2).isChecked = preferences.getBoolean("1", true)
        view.findViewById<CheckBox>(R.id.checkbox_3).isChecked = preferences.getBoolean("2", true)
        view.findViewById<CheckBox>(R.id.checkbox_4).isChecked = preferences.getBoolean("3", true)
        val radius = (preferences.getInt("radius", 0)).toString()
        view.findViewById<EditText>(R.id.radius).setText(radius)

        view.findViewById<CheckBox>(R.id.checkbox_1).setOnCheckedChangeListener { buttonView, isChecked ->
            val preferences: SharedPreferences = context!!.getSharedPreferences(
                    "FILTERMAP",
                    Context.MODE_PRIVATE
            )
            val editor = preferences.edit()
            editor.putBoolean("0", isChecked)
            editor.apply()
        }

        view.findViewById<CheckBox>(R.id.checkbox_2).setOnCheckedChangeListener { buttonView, isChecked ->
            val preferences: SharedPreferences = context!!.getSharedPreferences(
                    "FILTERMAP",
                    Context.MODE_PRIVATE
            )
            val editor = preferences.edit()
            editor.putBoolean("1", isChecked)
            editor.apply()
        }

        view.findViewById<CheckBox>(R.id.checkbox_3).setOnCheckedChangeListener { buttonView, isChecked ->
            val preferences: SharedPreferences = context!!.getSharedPreferences(
                    "FILTERMAP",
                    Context.MODE_PRIVATE
            )
            val editor = preferences.edit()
            editor.putBoolean("2", isChecked)
            editor.apply()
        }
        view.findViewById<CheckBox>(R.id.checkbox_4).setOnCheckedChangeListener { buttonView, isChecked ->
            val preferences: SharedPreferences = context!!.getSharedPreferences(
                    "FILTERMAP",
                    Context.MODE_PRIVATE
            )
            val editor = preferences.edit()
            editor.putBoolean("3", isChecked)
            editor.apply()
        }
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val dialog = dialog as BottomSheetDialog? ?: return
                val behavior = dialog.behavior
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.peekHeight = 0
                behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    }

                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                            dismiss()
                        }
                    }
                })
            }
        })

        view.findViewById<ImageView>(R.id.close).setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val preferences: SharedPreferences = context!!.getSharedPreferences("FILTERMAP", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putInt("radius", view!!.findViewById<EditText>(R.id.radius).text.toString().toInt())
        editor.apply()
        (activity  as MapaActivity).getMarkers()
    }

    fun filter(): FilterFragment {
        return this
    }


    fun show(fragmentManager: FragmentManager) {
        this.show(fragmentManager, TAG)
    }
}