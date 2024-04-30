package com.anisanurjanah.asclepius.view.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anisanurjanah.asclepius.data.remote.response.ArticlesItem
import com.anisanurjanah.asclepius.databinding.ItemArticleBinding
import com.anisanurjanah.asclepius.view.activity.DetailArticleActivity
import com.bumptech.glide.Glide

class HomeAdapter(private val articles: List<ArticlesItem>) : RecyclerView.Adapter<HomeAdapter.ArticleViewHolder>() {
    class ArticleViewHolder(private val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: ArticlesItem) {
            binding.apply {
                articleTitle.text = article.title
                articleSource.text = article.source?.name
                Glide.with(itemView.context)
                    .load(article.urlToImage)
                    .into(articleImage)
            }

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailArticleActivity::class.java)
                intent.putExtra(DetailArticleActivity.EXTRA_ARTICLE, article)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArticleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ArticleViewHolder(binding)
    }

    override fun getItemCount(): Int = minOf(4, articles.size)

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]
        holder.bind(article)
    }
}