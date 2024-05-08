package com.databridge.mybridge.domain.model.post

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class MediaItem(

	@field:SerializedName("file")
	val file: String? = null,

	@field:SerializedName("post")
	val post: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
) : Parcelable