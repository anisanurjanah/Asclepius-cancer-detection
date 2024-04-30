package com.anisanurjanah.asclepius.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anisanurjanah.asclepius.data.local.entity.History
import com.anisanurjanah.asclepius.databinding.ItemHistoryBinding
import com.bumptech.glide.Glide

class HistoryAdapter(private val listHistory: List<History>) : RecyclerView.Adapter<HistoryAdapter.ListViewHolder>() {
    class ListViewHolder(var binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = listHistory.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val history = listHistory[position]
        holder.binding.apply {
            historyLabel.text = "${history.label} ${history.score}"
            historyDate.text = history.timestamp
            Glide.with(holder.itemView.context)
                .load(history.imageUri)
                .into(holder.binding.historyImage)
        }
    }
}