package com.databridge.mybridge.ui.onboarding.utils

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.databridge.mybridge.R
import com.databridge.mybridge.base.loadRoundedImage
import com.databridge.mybridge.databinding.ItemUserSuggestionsBinding
import com.databridge.mybridge.domain.model.onboarding.UserSuggestionList


class SuggeastionsAdapter(var activity: Activity, val onItemClicked: (Int, View) -> Unit) :
    RecyclerView.Adapter<SuggeastionsAdapter.MyViewHolder>() {
    var datalList = ArrayList<UserSuggestionList>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SuggeastionsAdapter.MyViewHolder {
        val binding: ItemUserSuggestionsBinding = ItemUserSuggestionsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(
            binding!!
        )
    }

    override fun onBindViewHolder(holder: SuggeastionsAdapter.MyViewHolder, position: Int) {
        try {
            val data = datalList[position]
            with(holder.binding) {
                try {
                    tvName.text = data.displayName
                    tvRole.text = data.position
                    tvLocation.text = data.location
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                val profile = data.profilePic
                if (profile.isNullOrEmpty().not()) {
                    profile?.let {
                        ivPhotoPreview.loadRoundedImage(
                            it,
                            R.drawable.profile_frame
                        )
                    }
                } else tvImageName.text = data.profilePicChars ?: ""

                if (data.isFollowing)
                    tvFollow.text = activity.getString(R.string.following)
                else
                    tvFollow.text = activity.getString(R.string.follow)

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun swapList(datalList: ArrayList<UserSuggestionList>) {
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

    fun update(position: Int, cData: UserSuggestionList) {
        datalList[position] = cData
        notifyItemChanged(position)
    }

    inner class MyViewHolder(var binding: ItemUserSuggestionsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            // profile click
            binding.tvHandshake.setOnClickListener {
                onItemClicked(adapterPosition, binding.tvHandshake)
            }

            binding.tvFollow.setOnClickListener {
                onItemClicked(adapterPosition, binding.tvFollow)
            }
        }
    }
}