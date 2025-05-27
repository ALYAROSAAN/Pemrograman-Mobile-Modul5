package com.example.myanimelistapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AnimeViewModelFactory(private val source: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AnimeViewModel::class.java)) {
            return AnimeViewModel(source) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
