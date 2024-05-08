package com.databridge.mybridge.ui.profile.utils

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.databridge.mybridge.databinding.ItemProfileDetailBinding
import com.databridge.mybridge.domain.model.profile.ProfileDetail


class ProfileDetailAdapter(var activity: Activity, val onItemClicked: (Int, View) -> Unit) :
    RecyclerView.Adapter<ProfileDetailAdapter.MyViewHolder>() {
    var datalList = ArrayList<ProfileDetail>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileDetailAdapter.MyViewHolder {
        val binding: ItemProfileDetailBinding = ItemProfileDetailBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(
            binding!!
        )
    }

    override fun onBindViewHolder(holder: ProfileDetailAdapter.MyViewHolder, position: Int) {
        try {
            val data = datalList[position]
            with(holder.binding) {
                try {
                    tvName.text = data.name
                    tvValue.text = data.value
                    data.icon?.let { ivicon.setImageResource(it) }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun swapList(datalList: ArrayList<ProfileDetail>) {
        this.datalList.clear()
        this.datalList.addAll(datalList)
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return datalList.size
    }

    fun update(position: Int, cData: ProfileDetail) {
        datalList[position]= cData
        notifyItemChanged(position)
    }

    inner class MyViewHolder(var binding: ItemProfileDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            // profile click
//            binding.tvHandshake.setOnClickListener {
//                onItemClicked(adapterPosition, binding.tvHandshake)
//            }
//
//            binding.tvFollow.setOnClickListener {
//                onItemClicked(adapterPosition, binding.tvFollow)
//            }
        }
    }
}