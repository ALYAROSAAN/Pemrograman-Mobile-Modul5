package com.example.myanimelistapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myanimelistappxml.R

class AnimeAdapter(
    private val animeList: List<ListItem>,
    private val onDetailClick: (ListItem) -> Unit,
    private val onOpenUrlClick: (ListItem) -> Unit
) : RecyclerView.Adapter<AnimeAdapter.AnimeViewHolder>() {

    class AnimeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgAnime: ImageView = view.findViewById(R.id.imgAnime)
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvSubtitle: TextView = view.findViewById(R.id.tvSubtitle)
        val btnDetail: Button = view.findViewById(R.id.btnDetail)
        val btnOpenUrl: Button = view.findViewById(R.id.btnOpenUrl)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_anime, parent, false)
        return AnimeViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnimeViewHolder, position: Int) {
        val item = animeList[position]
        holder.imgAnime.setImageResource(item.imageResId)
        holder.tvTitle.text = item.title
        holder.tvSubtitle.text = item.subtitle
        holder.btnDetail.setOnClickListener { onDetailClick(item) }
        holder.btnOpenUrl.setOnClickListener { onOpenUrlClick(item) }
    }

    override fun getItemCount() = animeList.size
}
