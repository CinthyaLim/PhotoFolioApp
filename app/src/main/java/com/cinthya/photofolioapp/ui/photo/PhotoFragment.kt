package com.cinthya.photofolioapp.ui.photo

import android.content.ContentUris
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearSnapHelper
import com.cinthya.photofolioapp.data.database.UserFavorite
import com.cinthya.photofolioapp.data.response.PhotoItem
import com.cinthya.photofolioapp.databinding.FragmentPhotoBinding
import com.cinthya.photofolioapp.ui.ViewModelFactory


class PhotoFragment : Fragment() {

    private lateinit var binding: FragmentPhotoBinding
    private lateinit var viewModel: PhotoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val folderId = PhotoFragmentArgs.fromBundle(arguments as Bundle).folderId
        val fromWhatFragment = PhotoFragmentArgs.fromBundle(arguments as Bundle).fromWhatFragment
        val position = PhotoFragmentArgs.fromBundle(arguments as Bundle).position

        viewModel = obtainViewModel(requireActivity() as AppCompatActivity)

        when(fromWhatFragment){
            0 -> setUpDataFromAll(position)
            1 -> setUpDataFromFolder(folderId, position)
            2 -> setUpDataFromFavorite(position)
        }

    }

    private fun setUpDataFromFavorite(position: Int) {
        viewModel.getAllFavorites().observeOnce{
            if(it.isNotEmpty()){
                setUpFavoriteData(it,position)
            }
        }
    }

    private fun setUpFavoriteData(list: List<Long>, position: Int){
        viewModel.getAllFavoritePhotoGroupedByDate(list, requireActivity().contentResolver).observe(requireActivity()){
            if(it.isNotEmpty()){
                setUpRecyclerView(it,position)
            }
        }
    }

    private fun setUpDataFromFolder(folderId: String, position: Int) {
        viewModel.getPhotoInsideFolderByDate(folderId, requireActivity().contentResolver).observe(requireActivity()){
            if(it.isNotEmpty()){
                setUpRecyclerView(it, position)
            }else{
                
            }
        }
    }

    private fun setUpDataFromAll(position: Int) {
        viewModel.getAllPhotoGroupedByDate(requireActivity().contentResolver).observe(requireActivity()){
            if(it.isNotEmpty()){
                setUpRecyclerView(it, position)
            }else{

            }
        }
    }

    private fun setUpRecyclerView(photoZoom: Map<String, List<PhotoItem>>, position: Int) {
        val arrayOfSortedFlatten = photoZoom.values.flatten()
        val photoZoomAdapter = PhotoAdapter(arrayOfSortedFlatten)
        val snapHelper = LinearSnapHelper()

        viewModel.getAllFavorite().observe(requireActivity()){
            photoZoomAdapter.setData(it as MutableList<Long>)
        }

        binding.apply {
            rvPhotoZoom.adapter = photoZoomAdapter
            rvPhotoZoom.scrollToPosition(position)
            snapHelper.attachToRecyclerView(rvPhotoZoom)
        }

        photoZoomAdapter.setOnItemClickCallback(object :PhotoAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Long) {
                val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, data)
                shareImage(imageUri)
            }
            override fun onInfoClicked(data: Long) {
                val go = PhotoFragmentDirections.actionPhotoFragmentToDetailFragment(data)
                findNavController().navigate(go)
            }
            override fun onFavoriteClicked(data: Long) {
                Toast.makeText(requireActivity(), "Deleted from Favorite!", Toast.LENGTH_SHORT).show()
                viewModel.deleteFavorite(data)
            }

            override fun onFavoriteBorderClicked(data: Long) {
                Toast.makeText(requireActivity(), "Added to Favorite!", Toast.LENGTH_SHORT).show()
                viewModel.insert(UserFavorite(imageId = data))
            }

            override fun onBackButtonClicked() {
                requireActivity().onBackPressed()
            }
        })

    }

    private fun shareImage(imageUri: Uri) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_STREAM, imageUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        startActivity(Intent.createChooser(shareIntent, "Share Image"))
    }

    private fun obtainViewModel(activity: AppCompatActivity): PhotoViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[PhotoViewModel::class.java]
    }

    fun <T> LiveData<T>.observeOnce(observer: Observer<T>) {
        observeForever(object : Observer<T> {
            override fun onChanged(t: T) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }

}


