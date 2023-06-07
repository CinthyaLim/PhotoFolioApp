package com.cinthya.photofolioapp.ui.folder.list

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cinthya.photofolioapp.data.response.ItemPhoto
import com.cinthya.photofolioapp.data.response.PhotoItem
import com.cinthya.photofolioapp.databinding.ItemRowPhotoFolderListBinding

class FolderPhotoListAdapter(private val groupedMap: HashMap<String, List<PhotoItem>>, private val context: Context, private val rowCount: Int): RecyclerView.Adapter<FolderPhotoListAdapter.ViewHolder>(), PhotoInsideListAdapter.OnItemClickListener  {

    private var innerAdapter: PhotoInsideListAdapter? = null

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ViewHolder(private val itemBinding: ItemRowPhotoFolderListBinding):
        RecyclerView.ViewHolder(itemBinding.root){
        fun bindItem(content: ItemPhoto) {
            itemBinding.apply {
                tvPhotoDate.text = content.dateFormat
                if(content.photoInside!=null){
                    val photoAdapter = PhotoInsideListAdapter(content.photoInside)
                    setInnerAdapter(photoAdapter)
                    photoAdapter.setOuterPosition(adapterPosition)
                    Log.d("PHOTO ADAPTER", photoAdapter.toString())
                    itemBinding.rvInsideDateGroup.layoutManager = GridLayoutManager(context, rowCount)
                    itemBinding.rvInsideDateGroup.adapter = photoAdapter
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowPhotoFolderListBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val date = groupedMap.keys.toList()[position]
        val itemList = groupedMap[date]
        holder.bindItem(ItemPhoto(groupedMap.keys.toList()[position], itemList))
    }


    override fun getItemCount(): Int = groupedMap.keys.size

    fun setInnerAdapter(adapter: PhotoInsideListAdapter) {
        innerAdapter = adapter
        innerAdapter?.setOnItemClickListener(this)
    }

    override fun onItemClick(imageItem: PhotoItem) {
        val position = getPositionFromFlattenedList(imageItem)
        onItemClickCallback.onItemClicked(position)
    }

    private fun getPositionFromFlattenedList(imageItem: PhotoItem): Int {
        val flattenedList = groupedMap.values.flatten()
        return flattenedList.indexOf(imageItem)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Int)
    }

}