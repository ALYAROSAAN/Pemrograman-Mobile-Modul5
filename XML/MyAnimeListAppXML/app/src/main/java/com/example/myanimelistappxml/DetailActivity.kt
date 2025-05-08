package com.example.myanimelistapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myanimelistappxml.R

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val img = findViewById<ImageView>(R.id.imgDetail)
        val tvTitle = findViewById<TextView>(R.id.tvDetailTitle)
        val tvDesc = findViewById<TextView>(R.id.tvDetailDesc)

        val title = intent.getStringExtra("title")
        val desc = intent.getStringExtra("desc")
        val imageResId = intent.getIntExtra("imageResId", 0)

        tvTitle.text = title
        tvDesc.text = desc
        img.setImageResource(imageResId)
    }
}
