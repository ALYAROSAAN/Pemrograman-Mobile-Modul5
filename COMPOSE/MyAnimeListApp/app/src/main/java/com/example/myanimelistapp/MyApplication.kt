package com.example.myanimelistapp

import android.app.Application
import androidx.room.Room
import com.example.myanimelistapp.data.local.AppDatabase
import com.example.myanimelistapp.network.RetrofitInstance
import com.example.myanimelistapp.repository.MovieRepository

class MyApplication : Application() {

    lateinit var repository: MovieRepository
        private set

    override fun onCreate() {
        super.onCreate()

        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "movie_database"
        ).build()

        repository = MovieRepository(
            apiService = RetrofitInstance.api,
            movieDao = database.movieDao(),
            apiKey = "b073540aec31d15b415ff4876c8c0d37"
        )
    }
}
