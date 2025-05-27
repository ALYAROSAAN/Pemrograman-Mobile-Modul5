package com.example.myanimelistappxml

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myanimelistapp.AnimeAdapter
import com.example.myanimelistapp.MainViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("MainActivity onCreate() dimulai")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            viewModel.animeList.collect { list ->
                Timber.d("List dimuat dengan ${list.size} item:")
                list.forEach {
                    Timber.d("- ${it.title}")
                }
                recyclerView.adapter = AnimeAdapter(
                    list,
                    onDetailClick = { item ->
                        Timber.d("Tombol Detail diklik untuk: ${item.title}")
                        val intent = Intent(this@MainActivity, DetailActivity::class.java).apply {
                            putExtra("title", item.title)
                            putExtra("desc", item.detailDesc)
                            putExtra("imageResId", item.imageResId)
                        }
                        startActivity(intent)
                    },
                    onOpenUrlClick = { item ->
                        Timber.d("Tombol (Open URL) diklik untuk: ${item.title}")
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.url))
                        startActivity(intent)
                    }
                )
            }
        }
    }
}
