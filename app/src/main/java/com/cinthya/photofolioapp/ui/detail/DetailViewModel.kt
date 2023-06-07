package com.cinthya.photofolioapp.ui.detail

import android.content.ContentResolver
import androidx.lifecycle.ViewModel
import com.cinthya.photofolioapp.data.repository.PhotoRepository

class DetailViewModel(private val photoRepository: PhotoRepository = PhotoRepository()): ViewModel() {

    fun getImagesDetail(imageId : Long, contentResolver: ContentResolver) = photoRepository.getPhotoDetail(imageId, contentResolver)

}