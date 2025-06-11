package com.example.myanimelistapp

import android.app.Application
import androidx.room.Room
import com.example.myanimelistapp.data.local.AppDatabase
import com.example.myanimelistapp.network.RetrofitInstance
import com.example.myanimelistapp.repository.MovieRepository

class MyApplication : Application() {

    // Buat instance database dan repository di sini agar hanya ada satu di seluruh aplikasi
    lateinit var repository: MovieRepository
        private set

    override fun onCreate() {
        super.onCreate()

        // Buat database
        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "movie_database"
        ).build()

        // Buat repository dengan semua dependency-nya
        repository = MovieRepository(
            apiService = RetrofitInstance.api,
            movieDao = database.movieDao(),
            apiKey = "MASUKKAN_API_KEY_TMDB_ANDA_DI_SINI" // GANTI INI!
        )
    }
}