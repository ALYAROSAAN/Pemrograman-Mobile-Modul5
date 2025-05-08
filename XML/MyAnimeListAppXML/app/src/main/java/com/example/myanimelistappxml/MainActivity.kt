package com.example.myanimelistapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myanimelistappxml.R

class MainActivity : AppCompatActivity() {

    private val animeList = listOf(
        ListItem("Naruto", "Seorang ninja muda yang bermimpi menjadi Hokage", R.drawable.naruto,
            "Naruto adalah anime populer tentang ninja muda dengan semangat juang tinggi.", "https://naruto.com"),
        ListItem("One Piece", "Monkey D. Luffy berlayar bersama kru Bajak Laut Topi Jerami untuk mencari harta karun legendaris One Piece. Dengan kemampuan buah iblis dan semangat pantang menyerah, mereka menjelajahi lautan, melawan bajak laut, dan mengejar mimpi menjadi Raja Bajak Laut.", R.drawable.naruto,
            "Monkey D. Luffy berlayar bersama kru Bajak Laut Topi Jerami untuk mencari harta karun legendaris One Piece. Dengan kemampuan buah iblis dan semangat pantang menyerah, mereka menjelajahi lautan, melawan bajak laut, dan mengejar mimpi menjadi Raja Bajak Laut.", "https://naruto.com"),
        ListItem("Attack On Titan", "Dalam dunia di mana umat manusia terancam punah oleh para Titan pemakan manusia, Eren Yeager bersumpah untuk membalas dendam setelah desanya dihancurkan. Ia bergabung dengan militer dan mengungkap misteri kelam di balik keberadaan Titan dan sejarah manusia.", R.drawable.naruto,
            "Dalam dunia di mana umat manusia terancam punah oleh para Titan pemakan manusia, Eren Yeager bersumpah untuk membalas dendam setelah desanya dihancurkan. Ia bergabung dengan militer dan mengungkap misteri kelam di balik keberadaan Titan dan sejarah manusia.", "https://myanimelist.net/anime/16498/Shingeki_no_Kyojin"),
        ListItem("Demon Slayer", "Tanjiro Kamado menjadi pembasmi iblis setelah keluarganya dibantai dan adiknya berubah menjadi iblis. Bersama teman-temannya, Tanjiro menghadapi berbagai iblis berbahaya demi mencari cara menyelamatkan adiknya dan membalas kejahatan yang terjadi.", R.drawable.naruto,
            "Tanjiro Kamado menjadi pembasmi iblis setelah keluarganya dibantai dan adiknya berubah menjadi iblis. Bersama teman-temannya, Tanjiro menghadapi berbagai iblis berbahaya demi mencari cara menyelamatkan adiknya dan membalas kejahatan yang terjadi.", "https://myanimelist.net/anime/38000/Kimetsu_no_Yaiba"),
        ListItem("Jujutsu Kaisen", "Yuji Itadori, seorang siswa SMA dengan kekuatan fisik luar biasa, tanpa sengaja memakan jari iblis terkutuk Sukuna. Ia bergabung dengan sekolah Jujutsu untuk mengendalikan kekuatan itu dan melawan kutukan yang mengancam umat manusia.", R.drawable.naruto,
            "Yuji Itadori, seorang siswa SMA dengan kekuatan fisik luar biasa, tanpa sengaja memakan jari iblis terkutuk Sukuna. Ia bergabung dengan sekolah Jujutsu untuk mengendalikan kekuatan itu dan melawan kutukan yang mengancam umat manusia.", "https://myanimelist.net/anime/40748/Jujutsu_Kaisen"),
      )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = AnimeAdapter(animeList,
            onDetailClick = { item ->
                val intent = Intent(this, DetailActivity::class.java).apply {
                    putExtra("title", item.title)
                    putExtra("desc", item.detailDesc)
                    putExtra("imageResId", item.imageResId)
                }
                startActivity(intent)
            },
            onOpenUrlClick = { item ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.url))
                startActivity(intent)
            }
        )
    }
}
