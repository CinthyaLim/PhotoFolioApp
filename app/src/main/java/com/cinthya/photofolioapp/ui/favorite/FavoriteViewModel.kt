package com.cinthya.photofolioapp.ui.favorite

import android.app.Application
import android.content.ContentResolver
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.cinthya.photofolioapp.data.repository.FavoriteRepository
import com.cinthya.photofolioapp.data.repository.PhotoRepository

class FavoriteViewModel(private val photoRepository: PhotoRepository = PhotoRepository(), application: Application) : ViewModel()  {
    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    fun getAllFavorite(): LiveData<List<Long>> = mFavoriteRepository.getAllFavorite()

    fun getAllFavoritePhotoGroupedByDate(favoriteList: List<Long>,contentResolver: ContentResolver) =
        photoRepository.groupImageItemsByMonthAndYear(photoRepository.getAllImageByIdList(favoriteList, contentResolver))

}