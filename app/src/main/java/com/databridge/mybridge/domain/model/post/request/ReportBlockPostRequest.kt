package com.databridge.mybridge.domain.model.post.request

data class ReportBlockPostRequest(
    val post: Int,
    val reported_by: Int,
    val message: String,
)
