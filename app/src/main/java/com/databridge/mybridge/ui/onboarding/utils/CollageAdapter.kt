package com.databridge.mybridge.ui.onboarding.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.databridge.mybridge.domain.model.onboarding.CollageResults

class CollageAdapter(context: Context, private val dataList: ArrayList<CollageResults>) :
    ArrayAdapter<CollageResults>(context, android.R.layout.simple_list_item_1, dataList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view =
            convertView ?: inflater.inflate(android.R.layout.simple_list_item_1, parent, false)

        val text1 = view.findViewById<TextView>(android.R.id.text1)
        val sResponse = dataList[position]
        var txt = sResponse.name
        text1.text = txt

        return view
    }
}