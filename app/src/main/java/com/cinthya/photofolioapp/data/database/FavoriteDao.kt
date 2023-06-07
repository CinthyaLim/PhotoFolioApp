package com.cinthya.photofolioapp.data.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(userFavorite: UserFavorite)

    @Delete
    fun delete(userFavorite: UserFavorite)

    @Query("DELETE FROM userFavorite WHERE imageId = :imageId")
    fun deleteByImageId(imageId : Long)

    @Query("SELECT imageId from userFavorite")
    fun getAllFavorite(): LiveData<List<Long>>

    @Query("SELECT EXISTS(SELECT * from userFavorite WHERE imageId = :imageId)")
    fun checkIsFavorite(imageId: Long): LiveData<Boolean>
}