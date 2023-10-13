package com.example.pexelswallpaper.ui.fragments

import android.app.DownloadManager
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.pexelswallpaper.databinding.FragmentWallpaperDetailsBinding
import com.example.pexelswallpaper.utils.Constants.TAG
import com.example.pexelswallpaper.utils.addOnBackPressedCallback
import com.example.pexelswallpaper.utils.showToast
import java.io.File


class WallpaperDetails : Fragment() {
    private var _binding: FragmentWallpaperDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWallpaperDetailsBinding.inflate(inflater, container, false)
        addOnBackPressedCallback { findNavController().popBackStack() }

        val photoDetails = requireArguments().getString("photoOriginal")
        val height = requireArguments().getInt("height")
        val width = requireArguments().getInt("width")
        val photographer = requireArguments().getString("photographer")


        binding.PhotoAndPhotographerName.text = "Photographer Name : $photographer"
        binding.widthAndHeight.text = "Width & Height : $width x $height"
        binding.photographerContact.text = "Powered by https://www.pexels.com/"



        Glide.with(requireContext())
            .load(photoDetails)
            .listener(object :RequestListener<Drawable?>{
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable?>,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.progressBar2.visibility = View.GONE
                    showToast("Image failed to load: ${e!!.message}")
                    Log.d(TAG, "onLoadFailed: ${e.message}")
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable?>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.d(TAG, "onResourceReady: ")
                    binding.progressBar2.visibility = View.GONE
                    binding.btnDownload.visibility = View.VISIBLE
                    binding.pexelPhoto.setImageDrawable(resource)
                    return true
                }

            }).into(binding.pexelPhoto)

        binding.btnDownload.setOnClickListener {
            downloadImageNew("$photographer", photoDetails!!)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun downloadImageNew(filename: String, downloadUrlOfImage: String) {
        try {
            val dm = requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
            val downloadUri = Uri.parse(downloadUrlOfImage)
            val request = DownloadManager.Request(downloadUri)
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(filename)
                .setMimeType("image/jpeg")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_PICTURES,
                    File.separator + filename + ".jpg"
                )
            dm!!.enqueue(request)
            showToast("Download started.")
        } catch (e: Exception) {
            showToast("Download failed ${e.message}.")
        }
    }

}