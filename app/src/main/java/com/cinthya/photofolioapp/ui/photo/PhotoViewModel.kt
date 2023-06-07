package com.cinthya.photofolioapp.ui.photo

import android.app.Application
import android.content.ContentResolver
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.cinthya.photofolioapp.data.database.UserFavorite
import com.cinthya.photofolioapp.data.repository.FavoriteRepository
import com.cinthya.photofolioapp.data.repository.PhotoRepository

class PhotoViewModel(private val photoRepository: PhotoRepository = PhotoRepository(), application: Application) : ViewModel()  {

    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)
    fun getAllFavorite(): LiveData<List<Long>> = mFavoriteRepository.getAllFavorite()
    fun getAllFavorites(): LiveData<List<Long>> = mFavoriteRepository.getAllFavorite()

    fun insert(userFavorite: UserFavorite) {
        mFavoriteRepository.insert(userFavorite)
    }
    fun deleteFavorite(imageId: Long){
        mFavoriteRepository.deleteById(imageId)
    }
    fun getAllPhotoGroupedByDate(contentResolver: ContentResolver) = photoRepository.groupImageItemsByMonthAndYear(photoRepository.getAllImage(contentResolver))

    fun getPhotoInsideFolderByDate(folderId: String, contentResolver: ContentResolver) =
        photoRepository.groupImageItemsByMonthAndYear(photoRepository.getImageByFolderId(folderId, contentResolver))

    fun getAllFavoritePhotoGroupedByDate(favoriteList: List<Long>,contentResolver: ContentResolver) =
        photoRepository.groupImageItemsByMonthAndYear(photoRepository.getAllImageByIdList(favoriteList, contentResolver))

}