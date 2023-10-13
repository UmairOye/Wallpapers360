package com.example.pexelswallpaper.data.remote.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.pexelswallpaper.data.remote.api.ApiClient
import com.example.pexelswallpaper.data.remote.repository.PexlesRepo
import com.example.pexelswallpaper.models.Photo

class PexelsViewModel: ViewModel() {
    private val pexelsApi = ApiClient.pexelsAPI
    private val repo = PexlesRepo(pexelsApi)

    fun getSearchResults(query: String, apiKey: String, per_page: Int, page: Int): LiveData<List<Photo>>
    {
        return repo.searchPhotos(query, apiKey,per_page, page)
    }
}