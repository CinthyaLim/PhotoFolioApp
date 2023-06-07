package com.cinthya.photofolioapp.data.repository

import android.content.ContentResolver
import android.content.ContentUris
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cinthya.photofolioapp.data.response.PhotoDetail
import com.cinthya.photofolioapp.data.response.PhotoFolder
import com.cinthya.photofolioapp.data.response.PhotoItem
import java.text.DateFormatSymbols
import java.util.*

class PhotoRepository{

    fun getImagesGroupedByFolder(contentResolver: ContentResolver): LiveData<List<PhotoFolder>> {
        val liveData = MutableLiveData<List<PhotoFolder>>()
        val imageFolders = mutableListOf<PhotoFolder>()
        val projection = arrayOf(
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATA
        )
        val sortOrder = "${MediaStore.Images.Media.BUCKET_ID} DESC, ${MediaStore.Images.Media.DATE_MODIFIED} DESC"
        val selection = "${MediaStore.Images.Media.SIZE} > 0 AND ${MediaStore.Images.Media.MIME_TYPE} IN (${getSupportedImageMimeTypes()})"

        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            sortOrder
        )

        if(cursor != null && cursor.moveToFirst()) {
            val bucketIdColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)
            val bucketNameColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            val imagePathColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

            var imagePaths = mutableListOf<String>()

            cursor.moveToFirst()
            do{
                val bucketId = cursor.getString(bucketIdColumnIndex)
                val bucketName = cursor.getString(bucketNameColumnIndex)
                val imagePath = cursor.getString(imagePathColumnIndex)

                if(!imagePaths.contains(bucketId)){
                    imageFolders.add(PhotoFolder(bucketId, bucketName, imagePath, 1))
                    imagePaths.add(bucketId)
                }else{
                    for(i in 0 until imageFolders.size){
                        if(imageFolders[i].folderId == bucketId){
                            imageFolders[i].imageCount++
                        }
                    }
                }
            }while (cursor.moveToNext())

            val finalFolderSorted = imageFolders.sortedBy { it.folderName }
            liveData.value = finalFolderSorted
            cursor.close()
        }else{
            liveData.value = imageFolders
            cursor?.close()
        }

        return liveData
    }

    private fun getSupportedImageMimeTypes(): String {
        val mimeTypes = arrayOf(
            "image/jpeg",
            "image/png",
            "image/gif")
        return mimeTypes.joinToString(separator = ",") { "'$it'" }
    }

    fun getImageByFolderId(folderId: String, contentResolver: ContentResolver): List<PhotoItem>{
        val photoInsideBuckets = mutableListOf<PhotoItem>()
        val projection = arrayOf(
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media._ID
        )
        val selectionArgs = arrayOf(folderId)
        val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"
        val selection = "${MediaStore.Images.Media.BUCKET_ID} = ?"

        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )

        if(cursor != null && cursor.moveToFirst()) {
            val imagePathColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            val imageDateTaken = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)
            val idColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

            cursor.moveToFirst()
            do{
                val imagePath = cursor.getString(imagePathColumnIndex)

                photoInsideBuckets.add(PhotoItem(cursor.getLong(idColumnIndex), imagePath, cursor.getLong(imageDateTaken)))

            }while (cursor.moveToNext())

            cursor.close()
        }
        cursor?.close()
        return photoInsideBuckets
    }

    fun getAllImage(contentResolver: ContentResolver): List<PhotoItem>{
        val photoAll = mutableListOf<PhotoItem>()
        val projection = arrayOf(
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media._ID
        )
        val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"

        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )

        if(cursor != null && cursor.moveToFirst()) {
            val imagePathColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            val imageDateTaken = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)
            val idColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

            cursor.moveToFirst()
            do{
                val imagePath = cursor.getString(imagePathColumnIndex)

                photoAll.add(PhotoItem(cursor.getLong(idColumnIndex), imagePath, cursor.getLong(imageDateTaken)))

            }while (cursor.moveToNext())

            cursor.close()
        }
        cursor?.close()
        return photoAll
    }

    fun getAllImageByIdList(idList : List<Long>, contentResolver: ContentResolver): List<PhotoItem>{
        val photoAll = mutableListOf<PhotoItem>()
        val projection = arrayOf(
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media._ID
        )
        val selection = "${MediaStore.Images.Media._ID} IN (${idList.joinToString()})"
        val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"

        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            sortOrder
        )

        if(cursor != null && cursor.moveToFirst()) {
            val imagePathColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            val imageDateTaken = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)
            val idColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

            cursor.moveToFirst()
            do{
                val imagePath = cursor.getString(imagePathColumnIndex)

                photoAll.add(PhotoItem(cursor.getLong(idColumnIndex), imagePath, cursor.getLong(imageDateTaken)))

            }while (cursor.moveToNext())

            cursor.close()
        }
        cursor?.close()
        return photoAll
    }

    fun groupImageItemsByMonthAndYear(photoItems: List<PhotoItem>): LiveData<Map<String, List<PhotoItem>>> {
        val liveData = MutableLiveData<Map<String, List<PhotoItem>>>()
        val groupedItems = mutableMapOf<String, MutableList<PhotoItem>>()
        var index = 0

        val calendar = Calendar.getInstance()

        for(imageItem in photoItems) {
            calendar.timeInMillis = imageItem.dateTaken

            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)
            val monthName = DateFormatSymbols().months[month]

            val key = "$monthName - $year"
            val group = groupedItems[key]
            if (group != null) {
                group.add(imageItem)
            } else {
                val newGroup = mutableListOf(imageItem)
                groupedItems[key] = newGroup
            }

            index++
        }
        liveData.value = groupedItems
        return liveData
    }

    fun getPhotoDetail(photoId: Long, contentResolver: ContentResolver): LiveData<PhotoDetail>{
        val liveData = MutableLiveData<PhotoDetail>()
        val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, photoId)
        val projection = arrayOf(
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.HEIGHT
        )
        val cursor = contentResolver.query(imageUri, projection, null, null, null)

        if(cursor!=null && cursor.moveToFirst()) {
            val imagePathColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            val imageDateTaken = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)
            val idColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val sizeColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
            val widthColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH)
            val heightColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT)

            cursor.moveToFirst()
            val imagePath = cursor.getString(imagePathColumnIndex)
            val imageDate = cursor.getLong(imageDateTaken)
            val imageId = cursor.getLong(idColumnIndex)
            val imageName = cursor.getString(nameColumnIndex)
            val imageSize = cursor.getLong(sizeColumnIndex)
            val imageWidth = cursor.getInt(widthColumnIndex)
            val imageHeight = cursor.getInt(heightColumnIndex)

            val photoDetails = PhotoDetail(imageId, imageName, imagePath, imageDate, imageSize, imageHeight, imageWidth)
            liveData.value = photoDetails

            cursor.close()
        }
        cursor?.close()
        return liveData
    }
}