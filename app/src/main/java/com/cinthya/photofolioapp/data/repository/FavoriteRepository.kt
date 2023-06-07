package com.cinthya.photofolioapp.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.cinthya.photofolioapp.data.database.FavoriteDao
import com.cinthya.photofolioapp.data.database.FavoriteRoomDatabase
import com.cinthya.photofolioapp.data.database.UserFavorite
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {
    private val mFavoriteDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteRoomDatabase.getDatabase(application)
        mFavoriteDao = db.favoriteDao()
    }

    fun getAllFavorite(): LiveData<List<Long>> = mFavoriteDao.getAllFavorite()

    fun deleteById(imageId: Long){
        executorService.execute { mFavoriteDao.deleteByImageId(imageId)}
    }

    fun insert(userFavorite: UserFavorite) {
        executorService.execute { mFavoriteDao.insert(userFavorite) }
    }

}