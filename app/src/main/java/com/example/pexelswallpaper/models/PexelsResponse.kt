package com.example.pexelswallpaper.models

data class PexelsResponse (
    val page: Int,
    val perPage: Int,
    val photos: List<Photo>,
    val totalResults: Int,
    val nextPage: String?
)