package com.databridge.mybridge.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user")
data class Data(
    @PrimaryKey(autoGenerate = true) val uId: Int = 0,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("first_name")
    val firstName: String?,
)