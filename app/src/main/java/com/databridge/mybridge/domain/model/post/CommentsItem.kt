package com.databridge.mybridge.domain.model.post

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CommentsItem(

    @field:SerializedName("owner")
    val owner: UserProfileItem? = null,

//	@field:SerializedName("parent_comment")
//	val parentComment: Any? = null,// todo - remove If not getting data type

//	@field:SerializedName("comment_replies")
//	val commentReplies: List<Any?>? = null,// todo - remove If not getting data type

    @field:SerializedName("like_count")
    val likeCount: Int? = null,

//	@field:SerializedName("gif")
//	val gif: Any? = null,// todo - remove If not getting data type

    @field:SerializedName("liked_by")
    val likedBy: List<UserProfileItem?>? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("media")
    val media: List<MediaItem?>? = null,

    @field:SerializedName("reply_count")
    val replyCount: Int? = null,

    @field:SerializedName("body")
    val body: String? = null,

    @field:SerializedName("post")
    val post: Int? = null,

    @field:SerializedName("dislike_by")
	val dislikeBy: List<UserProfileItem?>? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("dislike_count")
    val dislikeCount: Int? = null
) : Parcelable