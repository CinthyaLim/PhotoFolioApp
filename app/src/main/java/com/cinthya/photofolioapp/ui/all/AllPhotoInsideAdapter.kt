package com.cinthya.photofolioapp.ui.all

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinthya.photofolioapp.data.response.PhotoItem
import com.cinthya.photofolioapp.databinding.ItemRowPhotoBinding

class AllPhotoInsideAdapter(private val list: List<PhotoItem>): RecyclerView.Adapter<AllPhotoInsideAdapter.ViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null
    private var outerPosition: Int = -1

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    inner class ViewHolder(private val itemBinding: ItemRowPhotoBinding):
        RecyclerView.ViewHolder(itemBinding.root){
        fun bindItem(content: PhotoItem) {
            itemBinding.apply {
                Glide.with(itemView)
                    .load(content.path)
                    .into(ivImageInsideFolder)
                itemView.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener?.onItemClick(content)
                    }
                }

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowPhotoBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(list[position])
    }

    fun setOuterPosition(position: Int) {
        outerPosition = position
    }

    override fun getItemCount() = list.size

    interface OnItemClickListener {
        fun onItemClick(imageItem: PhotoItem)
    }

}
