package com.example.pexelswallpaper.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.pexelswallpaper.R
import com.example.pexelswallpaper.adapters.HeaderAdapter
import com.example.pexelswallpaper.adapters.WallpaperAdapter
import com.example.pexelswallpaper.data.remote.viewModels.PexelsViewModel
import com.example.pexelswallpaper.databinding.FragmentHomeBinding
import com.example.pexelswallpaper.models.HeaderModel
import com.example.pexelswallpaper.models.Photo
import com.example.pexelswallpaper.utils.Constants.API_KEY

class Home : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModels: PexelsViewModel by activityViewModels()
    private var headerList: ArrayList<HeaderModel> = ArrayList()
    private lateinit var adapter: HeaderAdapter
    private lateinit var wallpaperAdapter: WallpaperAdapter
    private var pageCount: Int = 1
    private var topic: String = "Trending"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        headerList.apply {
            add(HeaderModel("Trending"))
            add(HeaderModel("New"))
            add(HeaderModel("Popular"))
            add(HeaderModel("Movies"))
            add(HeaderModel("Anime"))
            add(HeaderModel("Super Hero"))
            add(HeaderModel("Cars"))
            add(HeaderModel("Technologies"))
        }

        adapter = HeaderAdapter(requireContext())
        binding.recyclerView.adapter = adapter
        adapter.submitList(headerList)


        wallpaperAdapter = WallpaperAdapter()
        binding.wallpaperRecyclerview.adapter = wallpaperAdapter
        loadWallpapers(topic, pageCount)

        adapter.setOnClickListener(listener = object : HeaderAdapter.OnClickListener {
            override fun onItemClick(item: HeaderModel) {
                topic = item.name
                pageCount = 1
                binding.progressBar.visibility = View.VISIBLE
                loadWallpapers(topic, pageCount)
            }
        })


        binding.nextPage.setOnClickListener {
            loadWallpapers(topic, ++pageCount)
            binding.progressBar.visibility = View.VISIBLE
        }

        binding.prePage.setOnClickListener {
            if (pageCount > 1) {
                binding.progressBar.visibility = View.VISIBLE
                loadWallpapers(topic, --pageCount)
            }
        }



        binding.send.setOnClickListener {
            if (binding.edSearch.text.isNotEmpty()) {
                topic = binding.edSearch.text.toString()
                pageCount = 1
                binding.progressBar.visibility = View.VISIBLE
                loadWallpapers(topic, pageCount)
            }
        }

        wallpaperAdapter.setOnClickListener(listener = object : WallpaperAdapter.OnClickListener {
            override fun onItemClick(item: Photo) {
                val bundle = Bundle()
                bundle.putString("photoOriginal", item.src.original)
                bundle.putString("photographer", item.photographer)
                bundle.putInt("height", item.height)
                bundle.putInt("width", item.width)
                try {
                    findNavController().navigate(R.id.action_home2_to_wallpaperDetails, bundle)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }

        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun loadWallpapers(topic: String, page: Int) {
        viewModels.getSearchResults(topic, API_KEY, 50, page).observe(viewLifecycleOwner, Observer {
            binding.progressBar.visibility = View.GONE
            wallpaperAdapter.submitList(it)
            binding.layoutPages.visibility = View.VISIBLE
        })
    }
}