package com.example.myanimelistapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage // <-- DIPERBARUI: Menggunakan AsyncImage yang lebih modern
import com.example.myanimelistapp.MyApplication // <-- DITAMBAHKAN: Import Application class
import com.example.myanimelistapp.data.model.Movie // <-- DIPERBAIKI: Import data class yang benar
import com.example.myanimelistapp.ui.theme.MyAnimeListAppTheme
import com.example.myanimelistapp.viewmodel.MovieUiState // <-- DITAMBAHKAN: Import UI State
import com.example.myanimelistapp.viewmodel.MovieViewModel // <-- Ganti nama ViewModel jika perlu
import com.example.myanimelistapp.viewmodel.ViewModelFactory
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Dapatkan factory dari Application class. Ini cara dependency injection manual sederhana.
        val factory = ViewModelFactory((application as MyApplication).repository)

        setContent {
            MyAnimeListAppTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    MovieApp(factory = factory)
                }
            }
        }
    }
}

// === DIHAPUS ===
// Data class Movie yang ada di sini dihapus karena kita sudah punya yang benar di data.model

@Composable
fun MovieApp(factory: ViewModelFactory) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            // Passing factory ke ItemList
            ItemList(navController = navController, factory = factory)
        }
        composable(
            // Definisikan route dengan argumen
            route = "detail/{title}/{overview}/{posterPath}",
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("overview") { type = NavType.StringType },
                navArgument("posterPath") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            // Ambil argumen dari backStackEntry
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val overview = backStackEntry.arguments?.getString("overview") ?: ""
            // posterPath sudah di-encode, jadi kita tidak perlu decode di sini
            val posterPath = backStackEntry.arguments?.getString("posterPath") ?: ""

            DetailScreen(title = title, detail = overview, imageUrl = posterPath)
        }
    }
}


// === List Screen ===
// file: ui/MainActivity.kt

@Composable
fun ItemList(
    navController: NavHostController,
    factory: ViewModelFactory
) {
    val viewModel: MovieViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsState()

    // Periksa keseluruhan blok 'when' ini di kode Anda
    when (val state = uiState) {
        is MovieUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        // Pastikan Anda punya blok 'is MovieUiState.Success' seperti ini
        is MovieUiState.Success -> {

            // DAN PASTIKAN LazyColumn BERADA DI DALAM KURUNG KURAWAL BLOK SUCCESS INI
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Di sini 'state.movies' PASTI akan dikenali karena kita berada di dalam blok Success
                items(state.movies) { movie ->
                    MovieCard(movie = movie, navController = navController)
                }
            }
        }
        is MovieUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Error: ${state.message}")
            }
        }
    }
}

@Composable
fun MovieCard(movie: Movie, navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { // <-- DITAMBAHKAN: Membuat card bisa diklik
                // Encode posterPath untuk keamanan saat passing di URL
                val encodedPosterPath = URLEncoder.encode(movie.posterPath ?: "", StandardCharsets.UTF_8.toString())

                navController.navigate("detail/${movie.title}/${movie.overview}/$encodedPosterPath")
            },
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w200${movie.posterPath}",
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(100.dp)
                    .height(150.dp)
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = movie.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = movie.overview,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

// === Detail Screen ===
@Composable
fun DetailScreen(title: String, detail: String, imageUrl: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Gunakan AsyncImage untuk memuat gambar
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500${imageUrl}",
            contentDescription = title,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp) // Sesuaikan ukuran sesuai kebutuhan
                .clip(RoundedCornerShape(12.dp))
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = detail,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = Int.MAX_VALUE, // Tampilkan seluruh overview
            overflow = TextOverflow.Visible
        )
    }
}