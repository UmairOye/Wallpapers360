package com.example.pexelswallpaper.data.remote.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pexelswallpaper.data.remote.api.PexelsApi
import com.example.pexelswallpaper.models.PexelsResponse
import com.example.pexelswallpaper.models.Photo
import com.example.pexelswallpaper.utils.Constants.TAG
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PexlesRepo(private val apiService: PexelsApi) {
    fun searchPhotos(query: String, apiKey: String, per_page: Int, page: Int): LiveData<List<Photo>> {
        val photosLiveData = MutableLiveData<List<Photo>>()
        val call = apiService.searchPhotos(apiKey, query,per_page, page)
        call.enqueue(object : Callback<PexelsResponse> {
            override fun onResponse(call: Call<PexelsResponse>, response: Response<PexelsResponse>) {
                if (response.isSuccessful) {
                    val pexelsResponse = response.body()

                    pexelsResponse?.let {
                        photosLiveData.postValue(it.photos)
                    }
                }
            }

            override fun onFailure(call: Call<PexelsResponse>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }
        })

        return photosLiveData
    }

}