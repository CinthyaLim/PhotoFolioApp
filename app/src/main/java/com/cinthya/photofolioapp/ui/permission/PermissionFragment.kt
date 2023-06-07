package com.cinthya.photofolioapp.ui.permission

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.cinthya.photofolioapp.databinding.FragmentPermissionBinding


class PermissionFragment : Fragment() {

    private lateinit var binding: FragmentPermissionBinding
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPermissionBinding.inflate(inflater, container, false)

        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    val go = PermissionFragmentDirections.actionPermissionFragmentToFolderFragment()
                    findNavController().navigate(go)
                } else {
                    // Permission denied, show a message or take appropriate action
                }
            }

        // Find the button in your layout
        // Set an onClickListener to handle the button click
        binding.btnPermission.setOnClickListener {
            // Request the permission when the button is clicked
            requestPermission()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnPermission.setOnClickListener {
            requestPermission()
        }
    }

    private fun requestPermission() {
        val permission = Manifest.permission.READ_EXTERNAL_STORAGE

        if (ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED) {
            val go = PermissionFragmentDirections.actionPermissionFragmentToFolderFragment()
            findNavController().navigate(go)
        } else {
            // Permission not granted, request it
            requestPermissionLauncher.launch(permission)
        }
    }

}