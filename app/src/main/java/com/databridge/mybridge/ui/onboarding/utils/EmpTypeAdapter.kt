package com.databridge.mybridge.ui.onboarding.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.databridge.mybridge.R
import com.databridge.mybridge.domain.model.onboarding.EmployeeType

class EmpTypeAdapter (context: Context, private val items: List<EmployeeType>) :
    ArrayAdapter<EmployeeType>(context, R.layout.custom_spinner_item, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createCustomView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createCustomView(position, convertView, parent)
    }

    private fun createCustomView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val view = convertView ?: inflater.inflate(R.layout.custom_spinner_item, parent, false)
        val textView = view.findViewById<TextView>(R.id.textView)
        textView.text = items[position].name
        return view
    }
}