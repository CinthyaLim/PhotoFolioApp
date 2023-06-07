package com.cinthya.photofolioapp.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.cinthya.photofolioapp.data.response.PhotoDetail
import com.cinthya.photofolioapp.databinding.FragmentDetailBinding
import java.text.SimpleDateFormat
import java.util.*


class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val viewModel : DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val photoId = DetailFragmentArgs.fromBundle(arguments as Bundle).id

        setUpData(photoId)

        binding.layoutHeaderDetailFragment.btnBackHeader.setOnClickListener {
            requireActivity().onBackPressed()
        }

    }

    private fun setUpData(photoId: Long) {
        viewModel.getImagesDetail(photoId, requireActivity().contentResolver).observe(requireActivity()){
            if(it!=null){
                setUpView(it)
            }else{

            }
        }
    }

    private fun setUpView(photoDetail: PhotoDetail) {

        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val formattedDateTime = dateFormat.format(Date(photoDetail.date))

        binding.apply {
            tvNameDetail.text = photoDetail.name
            tvPathDetail.text = photoDetail.path
            tvSizeDetail.text = photoDetail.size.toFormattedSize()
            tvTimeDetail.text = formattedDateTime
            tvHeightWidthDetail.text = "${photoDetail.height}px x ${photoDetail.width}px"
        }
    }

    fun Long.toFormattedSize(): String {
        val kb = this / 1024
        return when {
            kb < 1024 -> "$kb KB"
            else -> "${kb / 1024} MB"
        }
    }

}