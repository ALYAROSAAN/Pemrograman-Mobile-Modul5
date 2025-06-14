package com.example.myanimelistapp.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val id: Int,
    val title: String,
    @SerialName("poster_path") 
    val posterPath: String?,
    @SerialName("overview")
    val overview: String
)

