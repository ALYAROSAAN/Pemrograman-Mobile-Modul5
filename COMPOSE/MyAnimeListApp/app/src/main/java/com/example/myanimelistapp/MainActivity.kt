package com.example.myanimelistapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.myanimelistapp.ui.theme.MyAnimeListAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAnimeListAppTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "list") {

                    // ✅ Rute detail menerima 3 argumen
                    composable(
                        "detail/{title}/{detail}/{imageRes}",
                        arguments = listOf(
                            navArgument("title") { type = NavType.StringType },
                            navArgument("detail") { type = NavType.StringType },
                            navArgument("imageRes") { type = NavType.IntType }
                        )
                    ) { backStackEntry ->
                        val title = backStackEntry.arguments?.getString("title") ?: ""
                        val detail = backStackEntry.arguments?.getString("detail") ?: ""
                        val imageRes = backStackEntry.arguments?.getInt("imageRes") ?: R.drawable.naruto
                        DetailScreen(title, detail, imageRes)
                    }

                    composable("list") {
                        ItemList(navController)
                    }
                }
            }
        }
    }
}

// === Data class ===
data class ListItem(
    val title: String,
    val subtitle: String,
    val detail: String,
    val imageRes: Int,
    val url: String
)

// === Sample data ===
val sampleItems = listOf(
    ListItem("Naruto", "\"Naruto Uzumaki adalah seorang ninja remaja yang bercita-cita menjadi Hokage, pemimpin desa Konoha. Ia menyimpan kekuatan rubah berekor sembilan dalam tubuhnya, dan harus menghadapi penolakan, musuh kuat, serta perjalanan keras untuk membuktikan dirinya.\"\n", "\"Naruto Uzumaki adalah seorang ninja remaja yang bercita-cita menjadi Hokage, pemimpin desa Konoha. Ia menyimpan kekuatan rubah berekor sembilan dalam tubuhnya, dan harus menghadapi penolakan, musuh kuat, serta perjalanan keras untuk membuktikan dirinya.\"\n", R.drawable.naruto, "https://myanimelist.net/anime/20/Naruto"),
    ListItem("One Piece", "\"Monkey D. Luffy berlayar bersama kru Bajak Laut Topi Jerami untuk mencari harta karun legendaris One Piece. Dengan kemampuan buah iblis dan semangat pantang menyerah, mereka menjelajahi lautan, melawan bajak laut, dan mengejar mimpi menjadi Raja Bajak Laut.\"\n", "\"Monkey D. Luffy berlayar bersama kru Bajak Laut Topi Jerami untuk mencari harta karun legendaris One Piece. Dengan kemampuan buah iblis dan semangat pantang menyerah, mereka menjelajahi lautan, melawan bajak laut, dan mengejar mimpi menjadi Raja Bajak Laut.\"\n", R.drawable.op, "https://myanimelist.net/anime/21/One_Piece"),
    ListItem("Attack on Titan", "\"Dalam dunia di mana umat manusia terancam punah oleh para Titan pemakan manusia, Eren Yeager bersumpah untuk membalas dendam setelah desanya dihancurkan. Ia bergabung dengan militer dan mengungkap misteri kelam di balik keberadaan Titan dan sejarah manusia.\"\n", "\"Dalam dunia di mana umat manusia terancam punah oleh para Titan pemakan manusia, Eren Yeager bersumpah untuk membalas dendam setelah desanya dihancurkan. Ia bergabung dengan militer dan mengungkap misteri kelam di balik keberadaan Titan dan sejarah manusia.\"\n", R.drawable.aot, "https://myanimelist.net/anime/16498/Shingeki_no_Kyojin"),
    ListItem("Demon Slayer", "\"Tanjiro Kamado menjadi pembasmi iblis setelah keluarganya dibantai dan adiknya berubah menjadi iblis. Bersama teman-temannya, Tanjiro menghadapi berbagai iblis berbahaya demi mencari cara menyelamatkan adiknya dan membalas kejahatan yang terjadi.\"\n", "\"Tanjiro Kamado menjadi pembasmi iblis setelah keluarganya dibantai dan adiknya berubah menjadi iblis. Bersama teman-temannya, Tanjiro menghadapi berbagai iblis berbahaya demi mencari cara menyelamatkan adiknya dan membalas kejahatan yang terjadi.\"\n", R.drawable.kny, "https://myanimelist.net/anime/38000/Kimetsu_no_Yaiba"),
    ListItem("Jujutsu Kaisen", "\"Yuji Itadori, seorang siswa SMA dengan kekuatan fisik luar biasa, tanpa sengaja memakan jari iblis terkutuk Sukuna. Ia bergabung dengan sekolah Jujutsu untuk mengendalikan kekuatan itu dan melawan kutukan yang mengancam umat manusia.\"\n", "\"Yuji Itadori, seorang siswa SMA dengan kekuatan fisik luar biasa, tanpa sengaja memakan jari iblis terkutuk Sukuna. Ia bergabung dengan sekolah Jujutsu untuk mengendalikan kekuatan itu dan melawan kutukan yang mengancam umat manusia.\"\n", R.drawable.jjk, "https://myanimelist.net/anime/40748/Jujutsu_Kaisen")
)

// === List Screen ===
@Composable
fun ItemList(navController: NavHostController) {
    val context = LocalContext.current
    LazyColumn(modifier = Modifier.padding(8.dp)) {
        items(sampleItems) { item ->
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
            ) {
                Row(modifier = Modifier.padding(16.dp)) {
                    // Gambar di kiri (portrait)
                    Image(
                        painter = painterResource(id = item.imageRes),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(150.dp)
                            .height(250.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    // Konten teks dan tombol di kanan
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(item.title, style = MaterialTheme.typography.titleMedium)
                        Text(
                            item.subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = Int.MAX_VALUE,
                            overflow = TextOverflow.Visible
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = {
                                    // ✅ Kirim imageRes ke halaman detail
                                    navController.navigate("detail/${item.title}/${item.detail}/${item.imageRes}")
                                },
                                modifier = Modifier.widthIn(min = 100.dp, max = 140.dp)
                            ) {
                                Text("Detail", style = MaterialTheme.typography.labelLarge)
                            }

                            Button(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.url))
                                    context.startActivity(intent)
                                },
                                modifier = Modifier.widthIn(min = 100.dp, max = 140.dp)
                            ) {
                                Text("Open URL", style = MaterialTheme.typography.labelLarge)
                            }
                        }
                    }
                }
            }
        }
    }
}

// === Detail Screen ===
@Composable
fun DetailScreen(title: String, detail: String, imageRes: Int) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
                .clip(RoundedCornerShape(12.dp))
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = title, style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = detail,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = Int.MAX_VALUE,
            overflow = TextOverflow.Visible
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyAnimeListAppTheme {
        ItemList(rememberNavController())
    }
}
