package com.example.myanimelistapp.repository

import com.example.myanimelistapp.network.RetrofitInstance
import com.example.myanimelistapp.data.local.MovieDao
import com.example.myanimelistapp.data.local.MovieEntity
import com.example.myanimelistapp.data.model.Movie
import com.example.myanimelistapp.network.MovieApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class MovieRepository(
    private val apiService: MovieApiService,
    private val movieDao: MovieDao,
    private val apiKey: String
) {
    fun getPopularMovies(): Flow<Result<List<Movie>>> = flow {
        emit(Result.Loading)

        // 1. Ambil data dari cache dulu
        val cachedMovies = movieDao.getAllMovies().first() // ambil data saat ini
        emit(Result.Success(cachedMovies.map { it.toMovie() })) // map dari Entity ke Model

        try {
            // 2. Ambil data dari network
            val response = apiService.getPopularMovies(apiKey)
            val moviesFromApi = response.results

            // 3. Hapus cache lama dan simpan data baru
            movieDao.deleteAllMovies()
            movieDao.insertMovies(moviesFromApi.map { it.toMovieEntity() })

            // Flow dari DAO akan otomatis emit data terbaru, jadi kita tidak perlu emit lagi di sini.
            // Jika tidak menggunakan flow dari DAO, kita bisa emit lagi data baru dari sini.

        } catch (e: Exception) {
            // 4. Jika network gagal, emit error. UI tetap punya data dari cache.
            emit(Result.Error("Gagal mengambil data dari jaringan: ${e.message}"))
        }
    }
}

// Tambahkan fungsi mapping di suatu tempat
fun MovieEntity.toMovie() = Movie(id, title, posterPath, overview)
fun Movie.toMovieEntity() = MovieEntity(id, title, posterPath, overview)