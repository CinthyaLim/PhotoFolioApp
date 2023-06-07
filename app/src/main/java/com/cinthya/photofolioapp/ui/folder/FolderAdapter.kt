package com.cinthya.photofolioapp.ui.folder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.cinthya.photofolioapp.data.response.PhotoFolder
import com.cinthya.photofolioapp.databinding.ItemPhotoFolderBinding

class FolderAdapter(private val photoFolderList: List<PhotoFolder>): RecyclerView.Adapter<FolderAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ViewHolder(private val itemBinding: ItemPhotoFolderBinding):
        RecyclerView.ViewHolder(itemBinding.root){
        fun bindItem(content: PhotoFolder) {
            itemBinding.apply {
                tvPhotoName.text = content.folderName
                tvPhotoCount.text = content.imageCount.toString()
                val requestOptions = RequestOptions().transform(RoundedCorners(20)) // Set the corner radius
                Glide.with(itemView)
                    .load(content.folderImagePaths)
                    .apply(requestOptions)
                    .into(ivPhotoFolder)
                itemView.setOnClickListener {
                    onItemClickCallback.onItemClicked(content.folderId, content.folderName)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPhotoFolderBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(photoFolderList[position])
    }

    override fun getItemCount(): Int = photoFolderList.size

    interface OnItemClickCallback {
        fun onItemClicked(data: String, folderName : String)
    }
}