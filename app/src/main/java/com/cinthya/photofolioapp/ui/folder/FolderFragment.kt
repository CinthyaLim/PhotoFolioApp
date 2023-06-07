package com.cinthya.photofolioapp.ui.folder

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.cinthya.photofolioapp.data.response.PhotoFolder
import com.cinthya.photofolioapp.databinding.FragmentFolderBinding


class FolderFragment : Fragment() {

    private lateinit var binding: FragmentFolderBinding
    private val viewModel: FolderViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFolderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkPermission()


    }

    private fun isPermissionGranted(): Boolean {
        val result = ContextCompat.checkSelfPermission(requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun checkPermission() {
        if(isPermissionGranted()) {
            callViewModel()
        } else {
            requestPermission()
        }
    }

    private fun requestPermission() {
        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                callViewModel()
            }
            else {
                val go = FolderFragmentDirections.actionFolderFragmentToPermissionFragment()
                findNavController().navigate(go)
            }
        }
        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun callViewModel(){
        viewModel.getImagesByFolders(requireActivity().contentResolver).observe(requireActivity()){
            if(it.isNotEmpty()){
                setUpRecyclerView(it)
            }else{

            }
        }
    }

    private fun setUpRecyclerView(listPhotoFolder: List<PhotoFolder>) {
        val folderAdapter = FolderAdapter(listPhotoFolder)
        binding.apply {
            rvPhotoFolder.layoutManager = GridLayoutManager(requireActivity(), 3)
            rvPhotoFolder.adapter = folderAdapter
        }

        folderAdapter.setOnItemClickCallback(object :FolderAdapter.OnItemClickCallback {
            override fun onItemClicked(data: String, folderName: String) {
                val go = FolderFragmentDirections.actionFolderFragmentToFolderPhotoListFragment(data, folderName)
                findNavController().navigate(go)
            }
        })
    }

}