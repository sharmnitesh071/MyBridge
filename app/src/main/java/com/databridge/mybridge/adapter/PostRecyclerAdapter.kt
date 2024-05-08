package com.databridge.mybridge.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.databridge.mybridge.R
import com.databridge.mybridge.base.convertDateTime
import com.databridge.mybridge.base.loadImage
import com.databridge.mybridge.base.setLikeDislikeCount
import com.databridge.mybridge.base.setLikeDislikeImage
import com.databridge.mybridge.base.setPostUserTitle
import com.databridge.mybridge.common.CommonUtil
import com.databridge.mybridge.common.Conts.EVENT_GENERAL
import com.databridge.mybridge.common.Conts.EVENT_WEBINAR
import com.databridge.mybridge.common.Conts.POST_TYPE_ARTICLE
import com.databridge.mybridge.common.Conts.POST_TYPE_EVENT
import com.databridge.mybridge.common.Conts.POST_TYPE_NORMAL
import com.databridge.mybridge.databinding.LayoutPostBinding
import com.databridge.mybridge.domain.model.post.PostItem

class PostRecyclerAdapter(
    private var list: MutableList<PostItem?>?,
    val context: Context,
    val email: String,
    val onItemClicked: (Int, View) -> Unit
) :
    RecyclerView.Adapter<PostRecyclerAdapter.MyHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostRecyclerAdapter.MyHolder {
        val binding = LayoutPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(binding)
    }

    override fun onBindViewHolder(holder: PostRecyclerAdapter.MyHolder, position: Int) {
        with(holder.binding) {
            // back button visibility Gone
            layoutHeader.imgBack.visibility = View.GONE

            val author = list!![position]?.author
            val taggedUsers = list!![position]?.taggedUsers
            val media = list!![position]?.media

            // region header
            layoutHeader.imgProfile.loadImage(author?.profilePic ?: "")
            layoutHeader.tvProfileName.setPostUserTitle(author, taggedUsers)
            layoutHeader.tvDesignation.text = author?.position ?: ""

            layoutHeader.tvDuration.text = CommonUtil.offsetFrom(list!![position]?.createdAt ?: "")

            // endregion

            // region content
            if (list!![position]?.content.isNullOrEmpty()) {
                layoutDescription.tvDesignation.visibility = View.GONE
            } else {
                layoutDescription.tvDesignation.visibility = View.VISIBLE
                layoutDescription.tvDesignation.text = list!![position]?.content
            }
            // endregion

            // region post, event and article
            // post visibility
            layoutImage.imgPost.visibility = View.GONE
            // event visibility
            layoutHeader.imgEvents.visibility = View.GONE
            layoutHeader.tvEventTitle.visibility = View.GONE
            layoutDescription.tvPostTitle.visibility = View.GONE
            layoutButton.btnRSVP.visibility = View.GONE
            layoutButton.tvRSVP.visibility = View.GONE
            // article visibility
            if (list!![position]?.type == POST_TYPE_NORMAL) {
                // by default all visibility GONE here .. gif, image, video
                if (list!![position]?.gif.isNullOrEmpty().not()) {
                    // gif
                } else if (media.isNullOrEmpty().not()) {
                    if (CommonUtil.isVideoType(media!![0]?.file ?: "")) { //todo
                        // video
                    } else {
                        // image
                        layoutImage.imgPost.visibility = View.VISIBLE
                        layoutImage.imgPost.loadImage(
                            media[0]?.file ?: ""
                        ) //todo list image inside recyclerview
                    }
                }
            } else if (list!![position]?.type == POST_TYPE_EVENT) {
                // event
                layoutHeader.imgEvents.visibility = View.VISIBLE
                layoutHeader.tvEventTitle.visibility = View.VISIBLE
                if (CommonUtil.rsvpButtonEnable(list!![position]?.attendees, email)) {
                    layoutButton.btnRSVP.visibility = View.GONE
                    layoutButton.tvRSVP.visibility = View.VISIBLE

                    if (list!![position]?.eventType == EVENT_GENERAL) {
                        layoutButton.tvRSVP.text = context.getString(R.string.rsvp_ed)
                    } else if (list!![position]?.eventType == EVENT_WEBINAR) {
                        layoutButton.tvRSVP.text = context.getString(R.string.registered)
                    }
                } else {
                    layoutButton.btnRSVP.visibility = View.VISIBLE
                    layoutButton.tvRSVP.visibility = View.GONE

                    if (list!![position]?.eventType == EVENT_GENERAL) {
                        layoutButton.btnRSVP.text = context.getString(R.string.rsvp)
                    } else if (list!![position]?.eventType == EVENT_WEBINAR) {
                        layoutButton.btnRSVP.text = context.getString(R.string.register)
                    }
                }

                layoutHeader.tvEventTitle.convertDateTime(
                    list!![position]?.startTime,
                    list!![position]?.timezone ?: "UTC"
                )

                list!![position]?.title.let {
                    layoutDescription.tvPostTitle.visibility = View.VISIBLE
                    layoutDescription.tvPostTitle.text = list!![position]?.title
                }
            } else if (list!![position]?.type == POST_TYPE_ARTICLE) {
                // article
            }
            // endregion

            // region count
            // share count
            if (list!![position]?.shareCount == 0) {
                layoutCount.tvShareCount.visibility = View.GONE
                layoutCount.tvShareTitle.visibility = View.GONE
            } else {
                layoutCount.tvShareCount.text = list!![position]?.shareCount.toString()

                layoutCount.tvShareCount.visibility = View.VISIBLE
                layoutCount.tvShareTitle.visibility = View.VISIBLE
            }
            // comment count
            if (list!![position]?.commentCount == 0) {
                layoutCount.tvCommentCount.visibility = View.GONE
                layoutCount.tvCommentTitle.visibility = View.GONE
            } else {
                layoutCount.tvCommentCount.text = list!![position]?.commentCount.toString()

                layoutCount.tvCommentCount.visibility = View.VISIBLE
                layoutCount.tvCommentTitle.visibility = View.VISIBLE
            }
            // like and dislike count
            if (list!![position]?.likeCount == 0 && list!![position]?.dislikeCount == 0) {
                layoutCount.tvLikeDislikeCount.visibility = View.GONE
                layoutCount.imgAgreeDisagree.visibility = View.GONE
            } else {
                // set like and dislike value
                layoutCount.tvLikeDislikeCount.visibility = View.VISIBLE
                layoutCount.imgAgreeDisagree.visibility = View.VISIBLE
                layoutCount.tvLikeDislikeCount.setLikeDislikeCount(
                    list!![position]?.likeCount ?: 0,
                    list!![position]?.dislikeCount ?: 0
                )
                layoutCount.imgAgreeDisagree.setLikeDislikeImage(
                    list!![position]?.likeCount ?: 0,
                    list!![position]?.dislikeCount ?: 0
                )
            }

            // endregion

            // region agree and disagree button
            layoutButton.btnAgree.isSelected =
                CommonUtil.ownPostLiked(list!![position]?.likedBy, email)

            layoutButton.btnDisagree.isSelected =
                CommonUtil.ownPostDisliked(list!![position]?.dislikeBy, email)
            // endregion
        }

    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    inner class MyHolder(val binding: LayoutPostBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            // profile click
            binding.layoutHeader.imgProfile.setOnClickListener {
                onItemClicked(adapterPosition, binding.layoutHeader.imgProfile)
            }
            binding.layoutHeader.tvProfileName.setOnClickListener {
                onItemClicked(adapterPosition, binding.layoutHeader.tvProfileName)
            }
            binding.layoutHeader.tvDesignation.setOnClickListener {
                onItemClicked(adapterPosition, binding.layoutHeader.tvDesignation)
            }
            binding.layoutHeader.tvDuration.setOnClickListener {
                onItemClicked(adapterPosition, binding.layoutHeader.tvDuration)
            }
            // option menu click
            binding.layoutHeader.imgMoreInfo.setOnClickListener {
                onItemClicked(adapterPosition, binding.layoutHeader.imgMoreInfo)
            }
            // share count click
            binding.layoutCount.tvShareCount.setOnClickListener {
                onItemClicked(adapterPosition, binding.layoutCount.tvShareCount)
            }
            binding.layoutCount.tvShareTitle.setOnClickListener {
                onItemClicked(adapterPosition, binding.layoutCount.tvShareTitle)
            }
            // comment count click
            binding.layoutCount.tvCommentCount.setOnClickListener {
                onItemClicked(adapterPosition, binding.layoutCount.tvCommentCount)
            }
            binding.layoutCount.tvCommentTitle.setOnClickListener {
                onItemClicked(adapterPosition, binding.layoutCount.tvCommentTitle)
            }
            // agree button click
            binding.layoutButton.btnAgree.setOnClickListener {
                onItemClicked(adapterPosition, binding.layoutButton.btnAgree)
            }
            // disagree button click
            binding.layoutButton.btnDisagree.setOnClickListener {
                onItemClicked(adapterPosition, binding.layoutButton.btnDisagree)
            }
            // button comment click
            binding.layoutButton.btnComment.setOnClickListener {
                onItemClicked(adapterPosition, binding.layoutButton.btnComment)
            }
            // button share click
            binding.layoutButton.btnShare.setOnClickListener {
                onItemClicked(adapterPosition, binding.layoutButton.btnShare)
            }
            // event rsvp & register
            binding.layoutButton.btnRSVP.setOnClickListener {
                onItemClicked(adapterPosition, binding.layoutButton.btnRSVP)
            }
        }
    }

}