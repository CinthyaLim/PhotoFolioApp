package com.cinthya.photofolioapp.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cinthya.photofolioapp.ui.all.AllPhotoViewModel
import com.cinthya.photofolioapp.ui.detail.DetailViewModel
import com.cinthya.photofolioapp.ui.favorite.FavoriteViewModel
import com.cinthya.photofolioapp.ui.folder.FolderViewModel
import com.cinthya.photofolioapp.ui.folder.list.FolderPhotoListViewModel
import com.cinthya.photofolioapp.ui.photo.PhotoViewModel

class ViewModelFactory private constructor(private val mApplication: Application) : ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(application: Application): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(application)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(FolderViewModel::class.java) -> {
                FolderViewModel() as T
            }
            modelClass.isAssignableFrom(FolderPhotoListViewModel::class.java) -> {
                FolderPhotoListViewModel() as T
            }
            modelClass.isAssignableFrom(AllPhotoViewModel::class.java) -> {
                AllPhotoViewModel() as T
            }
            modelClass.isAssignableFrom(PhotoViewModel::class.java) -> {
                PhotoViewModel(application = mApplication) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel() as T
            }
            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> {
                FavoriteViewModel(application = mApplication) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}