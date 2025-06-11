package com.example.myanimelistapp.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import coil.compose.AsyncImage
import com.example.myanimelistapp.MyApplication
import com.example.myanimelistapp.data.model.Movie
import com.example.myanimelistapp.ui.theme.MyAnimeListAppTheme
import com.example.myanimelistapp.viewmodel.MovieUiState
import com.example.myanimelistapp.viewmodel.MovieViewModel
import com.example.myanimelistapp.viewmodel.ViewModelFactory
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = ViewModelFactory((application as MyApplication).repository)
        setContent {
            MyAnimeListAppTheme(darkTheme = false) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    MovieApp(factory = factory)
                }
            }
        }
    }
}

@Composable
fun MovieApp(factory: ViewModelFactory) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            ItemList(navController = navController, factory = factory)
        }
        composable(
            route = "detail/{title}/{overview}/{posterPath}",
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("overview") { type = NavType.StringType },
                navArgument("posterPath") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val overview = backStackEntry.arguments?.getString("overview") ?: ""
            val posterPath = backStackEntry.arguments?.getString("posterPath") ?: ""
            DetailScreen(title = title, detail = overview, imageUrl = posterPath)
        }
    }
}

@Composable
fun ItemList(
    navController: NavHostController,
    factory: ViewModelFactory
) {
    val viewModel: MovieViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is MovieUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is MovieUiState.Success -> {
            LazyColumn(modifier = Modifier.padding(8.dp)) {
                items(state.movies) { movie ->
                    // Memanggil MovieCard yang sudah disesuaikan
                    MovieCard(movie = movie, navController = navController)
                }
            }
        }
        is MovieUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Gagal memuat data: ${state.message}")
            }
        }
    }
}

@Composable
fun MovieCard(movie: Movie, navController: NavHostController) {
    val context = LocalContext.current

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF0F0F0)
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            // Menggunakan AsyncImage untuk memuat gambar dari URL
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500${movie.posterPath}",
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(150.dp)
                    .height(250.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(), // Mengisi tinggi yang tersisa
                // Mengatur jarak antar elemen di dalam Column ini
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Kolom untuk Teks (Judul dan Overview)
                Column {
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = movie.overview, // Menggunakan overview sebagai teks utama
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 5, 
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Kolom untuk Tombol (agar tetap di bawah)
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            val encodedPosterPath = URLEncoder.encode(movie.posterPath ?: "", StandardCharsets.UTF_8.toString())
                            // Navigasi dengan data dari API
                            navController.navigate("detail/${movie.title}/${movie.overview}/$encodedPosterPath")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3A4C8B),
                            contentColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth() // Dibuat fill width agar rapi
                    ) {
                        Text("Detail", style = MaterialTheme.typography.labelLarge)
                    }

                    Button(
                        onClick = {
                            val url = "https://www.themoviedb.org/search?query=${URLEncoder.encode(movie.title, StandardCharsets.UTF_8.toString())}"
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3A4C8B),
                            contentColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Open URL", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
    }
}

@Composable
fun DetailScreen(title: String, detail: String, imageUrl: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500${imageUrl}",
            contentDescription = title,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .clip(RoundedCornerShape(12.dp))
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = detail, style = MaterialTheme.typography.bodyLarge)
    }
}
