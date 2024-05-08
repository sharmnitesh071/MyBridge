package com.databridge.mybridge.ui.onboarding.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.databridge.mybridge.R
import com.databridge.mybridge.domain.model.onboarding.DateYear

class DateYearAdapter(context: Context, private val years: List<DateYear>) :
    ArrayAdapter<DateYear>(context, R.layout.custom_spinner_item, years) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.custom_spinner_item, parent, false)
        val year = years[position].value
        (view as? TextView)?.text = year.toString()
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }
}