package com.databridge.mybridge.domain.model.post

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostItem(

    @field:SerializedName("comment_count")
    val commentCount: Int? = null,

    @field:SerializedName("event_post_email")
    val eventPostEmail: Boolean? = null,

    @field:SerializedName("type")
    val type: Int? = null,

    @field:SerializedName("all_day_event")
    val allDayEvent: Boolean? = null,

    @field:SerializedName("blog_type")
    val blogType: Int? = null,

    @field:SerializedName("reply_control")
    val replyControl: Int? = null,

    @field:SerializedName("event_type")
    val eventType: String? = null,

    @field:SerializedName("reposts_by")
    val repostsBy: List<UserProfileItem?>? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("cover_image")
    val coverImage: String? = null,

    @field:SerializedName("slug")
    val slug: String? = null,

    @field:SerializedName("repost")
    val repost: RePostItem? = null,

    @field:SerializedName("event_status")
    val eventStatus: String? = null,

    @field:SerializedName("comment_by")
    val commentBy: List<UserProfileItem?>? = null,

    @field:SerializedName("like_count")
    val likeCount: Int? = null,

    @field:SerializedName("visibility")
    val visibility: Int? = null,

    @field:SerializedName("blog_status")
    val blogStatus: Int? = null,

    @field:SerializedName("author")
    val author: UserProfileItem? = null,

    @field:SerializedName("attendees")
    val attendees: List<Attendees?>? = null,

//    @field:SerializedName("search_score")
//    val searchScore: Any? = null, // todo - remove If not getting data type

    @field:SerializedName("tagged_user_ids")
    val taggedUserIds: List<Int?>? = null,

    @field:SerializedName("tagged_users")
    val taggedUsers: List<UserProfileItem?>? = null,

    @field:SerializedName("start_time")
    val startTime: String? = null,

//    @field:SerializedName("event_recording_url")
//    val eventRecordingUrl: Any? = null,// todo - remove If not getting data type

//    @field:SerializedName("suspended_till")
//    val suspendedTill: Any? = null,// todo - remove If not getting data type

    @field:SerializedName("event_security")
    val eventSecurity: String? = null,

//    @field:SerializedName("page")
//    val page: Any? = null,// todo - remove If not getting data type

    @field:SerializedName("excerpt")
    val excerpt: String? = null,

    @field:SerializedName("status")
    val status: Int? = null,

    @field:SerializedName("pinned")
    val pinned: Boolean? = null,

//    @field:SerializedName("event_file2")
//    val eventFile2: Any? = null,// todo - remove If not getting data type

//    @field:SerializedName("event_file1")
//    val eventFile1: Any? = null,// todo - remove If not getting data type

    @field:SerializedName("gif")
    val gif: String? = null,// todo - remove If not getting data type

    @field:SerializedName("timezone")
    val timezone: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("media")
    val media: List<MediaItem?>? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("follower_count")
    val followerCount: Int? = null,

    @field:SerializedName("content")
    val content: String? = null,

    @field:SerializedName("default")
    val default: Boolean? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("company")
    val company: Company? = null,

    @field:SerializedName("event_recording")
    val eventRecording: Boolean? = null,

    @field:SerializedName("comments")
    val comments: List<CommentsItem?>? = null,

    @field:SerializedName("ignored_by")
    val ignoredBy: List<Int?>? = null,

    @field:SerializedName("liked_by")
    val likedBy: List<UserProfileItem?>? = null,

    @field:SerializedName("end_time")
    val endTime: String? = null,

    @field:SerializedName("bookmark_user_ids")
    val bookmarkUserIds: List<Int?>? = null,

//    @field:SerializedName("location_type")
//    val locationType: Any? = null,// todo - remove If not getting data type

    @field:SerializedName("share_count")
    val shareCount: Int? = null,

    @field:SerializedName("readers")
    val readers: List<Int?>? = null,

    @field:SerializedName("dislike_by")
    val dislikeBy: List<UserProfileItem?>? = null,

    @field:SerializedName("location")
    val location: String? = null,

//    @field:SerializedName("feelings")
//    val feelings: Any? = null,// todo - remove If not getting data type

    @field:SerializedName("dislike_count")
    val dislikeCount: Int? = null
) : Parcelable