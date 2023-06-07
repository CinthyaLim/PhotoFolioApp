package com.cinthya.photofolioapp.data.response

data class PhotoFolder(
    val folderId: String,
    val folderName: String,
    val folderImagePaths: String,
    var imageCount : Int = 0
)
