package com.example.myanimelistapp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber

class AnimeViewModel(private val source: String) : ViewModel() {
    private val _animeList = MutableStateFlow<List<ListItem>>(emptyList())
    val animeList: StateFlow<List<ListItem>> = _animeList.asStateFlow()

    private val _selectedItem = MutableStateFlow<ListItem?>(null)
    val selectedItem: StateFlow<ListItem?> = _selectedItem.asStateFlow()

    init {
        _animeList.value = sampleItems
        Timber.d("Data item berhasil dimuat: ${sampleItems.size} item")
    }

    fun onDetailClicked(item: ListItem) {
        _selectedItem.value = item
        Timber.d("[$source] Tombol Detail ditekan: ${item.title}")
    }

    fun onUrlClicked(item: ListItem) {
        Timber.d("[$source] Tombol URL ditekan: ${item.title}")
    }

    fun logItemClick(item: ListItem, s: String) {

    }
}
