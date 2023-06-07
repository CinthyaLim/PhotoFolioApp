package com.cinthya.photofolioapp.ui.all

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cinthya.photofolioapp.data.response.PhotoItem
import com.cinthya.photofolioapp.databinding.FragmentAllPhotoBinding


class AllPhotoFragment : Fragment() {

    private lateinit var binding: FragmentAllPhotoBinding
    private val viewModel: AllPhotoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpData()
    }

    private fun setUpData() {
        viewModel.getAllPhotoGroupedByDate(requireActivity().contentResolver).observe(requireActivity()){
            if(it.isNotEmpty()){
                setUpRecyclerView(it)
            }else{

            }
        }
    }

    private fun setUpRecyclerView(allPhotoByDate: Map<String, List<PhotoItem>>?) {
        val allPhotoAdapter = AllPhotoAdapter(allPhotoByDate as HashMap<String, List<PhotoItem>>, requireContext(), calculateColumnCount())
        binding.apply {
            rvAllPhotoList.layoutManager = LinearLayoutManager(requireActivity())
            rvAllPhotoList.adapter = allPhotoAdapter
        }

        allPhotoAdapter.setOnItemClickCallback(object : AllPhotoAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Int) {
                val go = AllPhotoFragmentDirections.actionAllPhotoFragmentToPhotoFragment(0, "", data)
                findNavController().navigate(go)
            }
        })
    }

    private fun calculateColumnCount(): Int {
        val displayMetrics = resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        val columnWidthDp = 90 // Desired width of each grid item in dp
        val columnCount = (screenWidthDp / columnWidthDp).toInt()
        return if (columnCount >= 1) columnCount else 1 // Ensure at least 1 column
    }


}