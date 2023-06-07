package com.cinthya.photofolioapp.ui.folder

import android.content.ContentResolver
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.cinthya.photofolioapp.data.repository.PhotoRepository
import com.cinthya.photofolioapp.data.response.PhotoFolder

class FolderViewModel(private val photoRepository: PhotoRepository = PhotoRepository()): ViewModel() {

    fun getImagesByFolders(contentResolver: ContentResolver): LiveData<List<PhotoFolder>> = photoRepository.getImagesGroupedByFolder(contentResolver)

}