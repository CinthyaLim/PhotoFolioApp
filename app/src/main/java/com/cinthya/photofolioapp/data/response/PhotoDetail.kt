package com.cinthya.photofolioapp.data.response

data class PhotoDetail(
    val id: Long,
    val name: String,
    val path: String,
    val date: Long,
    val size: Long,
    val height: Int,
    val width: Int
)
