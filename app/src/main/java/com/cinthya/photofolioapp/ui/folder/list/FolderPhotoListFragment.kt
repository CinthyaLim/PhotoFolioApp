package com.cinthya.photofolioapp.ui.folder.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cinthya.photofolioapp.data.response.PhotoItem
import com.cinthya.photofolioapp.databinding.FragmentFolderPhotoListBinding


class FolderPhotoListFragment : Fragment() {

    private lateinit var binding: FragmentFolderPhotoListBinding
    private val viewModel: FolderPhotoListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFolderPhotoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val folderId = FolderPhotoListFragmentArgs.fromBundle(arguments as Bundle).folderId
        val folderName = FolderPhotoListFragmentArgs.fromBundle(arguments as Bundle).folderName

        binding.tvHeaderFolderList.text = folderName
        binding.btnBackToFolder.setOnClickListener {
            requireActivity().onBackPressed()
        }
        setUpData(folderId)
    }

    private fun setUpData(folderId : String) {
        viewModel.getPhotoInsideFolderByDate(folderId, requireActivity().contentResolver).observe(requireActivity()){
            if(it.isNotEmpty()){
                setUpRecyclerView(it, folderId)
            }else{

            }
        }
    }

    private fun setUpRecyclerView(dateDataPhoto: Map<String, List<PhotoItem>>?, folderId: String) {
        val photoInsideFolderAdapter = FolderPhotoListAdapter(dateDataPhoto as HashMap<String, List<PhotoItem>>, requireContext(), calculateColumnCount())
        binding.apply {
            rvAllPhotoList.layoutManager = LinearLayoutManager(requireActivity())
            rvAllPhotoList.adapter = photoInsideFolderAdapter
        }

        photoInsideFolderAdapter.setOnItemClickCallback(object : FolderPhotoListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Int) {
                val go = FolderPhotoListFragmentDirections.actionFolderPhotoListFragmentToPhotoFragment(1, folderId, data)
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