package com.cinthya.photofolioapp.ui.photo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinthya.photofolioapp.data.response.PhotoItem
import com.cinthya.photofolioapp.databinding.ItemRowPhotoZoomBinding

class PhotoAdapter(private val listImage : List<PhotoItem>):
    RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    private var headerVisible : Boolean = true
    private lateinit var onItemClickCallback: OnItemClickCallback
    private var favorited = mutableListOf<Long>()

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ViewHolder(private val itemBinding: ItemRowPhotoZoomBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bindItem(content: PhotoItem) {
            itemBinding.apply {
                Glide.with(itemView)
                    .load(content.path)
                    .into(ivImageZoom)
            }

            setFavoriteBuy(content.id, itemBinding.layoutItemFooter.ivBtnFavoriteBorder, itemBinding.layoutItemFooter.ivBtnFavoriteFill)
            itemBinding.ivImageZoom.setOnClickListener {
                if(headerVisible){
                    itemBinding.layoutItemHeader.root.visibility = View.INVISIBLE
                    itemBinding.layoutItemFooter.root.visibility = View.INVISIBLE
                    headerVisible = false
                }else{
                    itemBinding.layoutItemHeader.root.visibility = View.VISIBLE
                    itemBinding.layoutItemFooter.root.visibility = View.VISIBLE
                    headerVisible = true
                }
            }

            itemBinding.layoutItemFooter.ivBtnShare.setOnClickListener {
                onItemClickCallback.onItemClicked(content.id)
            }
            itemBinding.layoutItemFooter.ivBtnInfo.setOnClickListener {
                onItemClickCallback.onInfoClicked(content.id)
            }
            itemBinding.layoutItemFooter.ivBtnFavoriteFill.setOnClickListener {
                onItemClickCallback.onFavoriteClicked(content.id)
            }
            itemBinding.layoutItemFooter.ivBtnFavoriteBorder.setOnClickListener {
                onItemClickCallback.onFavoriteBorderClicked(content.id)
            }

            itemBinding.layoutItemHeader.btnBackHeader.setOnClickListener {
                onItemClickCallback.onBackButtonClicked()
            }
        }
    }

    private fun setFavoriteBuy(imageId: Long, favoriteBorder: ImageView, favoriteFill: ImageView) {
        if(imageId in favorited){
            favoriteBorder.visibility = View.INVISIBLE
            favoriteFill.visibility = View.VISIBLE
        }else{
            favoriteBorder.visibility = View.VISIBLE
            favoriteFill.visibility = View.INVISIBLE
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowPhotoZoomBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(listImage[position])
    }

    override fun getItemCount(): Int = listImage.size

    interface OnItemClickCallback {
        fun onItemClicked(data: Long)

        fun onInfoClicked(data: Long)

        fun onFavoriteClicked(data: Long)

        fun onFavoriteBorderClicked(data: Long)

        fun onBackButtonClicked()
    }

    fun setData(favoritedImageId: MutableList<Long>){
        this.favorited = favoritedImageId
        notifyDataSetChanged()
    }

}