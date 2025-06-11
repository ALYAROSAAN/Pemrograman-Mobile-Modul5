package com.example.myanimelistapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class MovieResponse(
    val page: Int,
    val results: List<Movie>
)