package com.cinthya.photofolioapp.ui.all

import android.content.ContentResolver
import androidx.lifecycle.ViewModel
import com.cinthya.photofolioapp.data.repository.PhotoRepository

class AllPhotoViewModel(private val photoRepository: PhotoRepository = PhotoRepository()): ViewModel() {

    fun getAllPhotoGroupedByDate(contentResolver: ContentResolver) = photoRepository.groupImageItemsByMonthAndYear(photoRepository.getAllImage(contentResolver))
}