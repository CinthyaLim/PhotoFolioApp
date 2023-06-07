package com.cinthya.photofolioapp.ui.folder.list

import android.content.ContentResolver
import androidx.lifecycle.ViewModel
import com.cinthya.photofolioapp.data.repository.PhotoRepository

class FolderPhotoListViewModel(private val photoRepository: PhotoRepository = PhotoRepository()): ViewModel() {

    fun getPhotoInsideFolderByDate(folderId: String, contentResolver: ContentResolver) =
        photoRepository.groupImageItemsByMonthAndYear(photoRepository.getImageByFolderId(folderId, contentResolver))
}