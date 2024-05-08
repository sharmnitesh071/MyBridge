package com.databridge.mybridge.ui.appintro.utils

import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.databridge.mybridge.R
import com.databridge.mybridge.ui.appintro.datamodel.AppIntroData


import java.util.*


class SlidingImageAdapter(private val context: Context) : PagerAdapter() {


    private val data = ArrayList<AppIntroData>()
    private val inflater: LayoutInflater
    private lateinit var mEventListener: EventListener

    init {
        inflater = LayoutInflater.from(context)
    }

    interface EventListener {
        fun onItemViewClicked(position: Int)
    }

    fun setEventListener(eventListener: EventListener) {
        mEventListener = eventListener
    }

    fun getItem(position: Int): AppIntroData {
        return data[position]
    }


    fun addAll(mData: MutableList<AppIntroData>) {
        data.clear()
        data.addAll(mData)
        notifyDataSetChanged()
    }

    fun clear() {
        data.clear()
        notifyDataSetChanged()
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return data.size
    }


    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val imageLayout = inflater.inflate(R.layout.item_app_intro, view, false)
        val item = data[position]

        assert(imageLayout != null)
        val ivIntro = imageLayout!!
            .findViewById<View>(R.id.ivIntro) as ImageView
//        val tvTitle: TextView = imageLayout!!.findViewById<TextView>(R.id.tvTitle) as TextView
        val tvDesc: TextView = imageLayout!!.findViewById<TextView>(R.id.tvDes) as TextView
        val llMain: ConstraintLayout =
            imageLayout!!.findViewById<TextView>(R.id.llMain) as ConstraintLayout
//
        ivIntro.setImageResource(item.imgsrc)
//        tvTitle.setText(item.title)
        tvDesc.setText(item.des)
//        var clr = R.color.color1
//        if (position == 1)
//            clr = R.color.color2
//        if (position == 2)
//            clr = R.color.color3
//        llMain.setBackgroundColor(ContextCompat.getColor(context, clr))

        view.addView(imageLayout, 0)

        return imageLayout
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}

    override fun saveState(): Parcelable? {
        return null
    }

}