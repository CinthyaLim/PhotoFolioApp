package com.cinthya.photofolioapp.ui.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cinthya.photofolioapp.data.response.PhotoItem
import com.cinthya.photofolioapp.databinding.FragmentFavoriteBinding
import com.cinthya.photofolioapp.ui.ViewModelFactory

class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var viewModel: FavoriteViewModel
    private lateinit var favoriteObserver : Observer<List<Long>>
    private lateinit var favoriteLiveData : LiveData<List<Long>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = obtainViewModel(requireActivity() as AppCompatActivity)
        favoriteLiveData = viewModel.getAllFavorite()
        setUpDataFavorite(favoriteLiveData)

    }

    private fun setUpDataFavorite(favoriteLiveData: LiveData<List<Long>>) {
        favoriteObserver = Observer{
            if(it!=null && it.isNotEmpty()){
                setUpData(it)
            }else{

            }
        }
        favoriteLiveData.observe(requireActivity(), favoriteObserver)
    }

    private fun setUpData(favoriteList: List<Long>) {
        viewModel.getAllFavoritePhotoGroupedByDate(favoriteList, requireActivity().contentResolver).observe(requireActivity()){
            if(it.isNotEmpty()){
                setUpRecyclerView(it)
            }else{

            }
        }
    }

    private fun setUpRecyclerView(photoItem: Map<String, List<PhotoItem>>?) {
        val favoriteInsideAdapter = FavoriteAdapter(photoItem as HashMap<String, List<PhotoItem>>, requireContext(), calculateColumnCount())
        binding.apply {
            rvAllFavoritePhoto.layoutManager = LinearLayoutManager(requireActivity())
            rvAllFavoritePhoto.adapter = favoriteInsideAdapter
        }

        favoriteInsideAdapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Int) {
                val go = FavoriteFragmentDirections.actionFavoriteFragmentToPhotoFragment(2, "", data)
                findNavController().navigate(go)
            }
        })
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }

    private fun calculateColumnCount(): Int {
        val displayMetrics = resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        val columnWidthDp = 90 // Desired width of each grid item in dp
        val columnCount = (screenWidthDp / columnWidthDp).toInt()
        return if (columnCount >= 1) columnCount else 1 // Ensure at least 1 column
    }

    override fun onPause() {
        super.onPause()
        favoriteLiveData.removeObserver(favoriteObserver)
    }
}