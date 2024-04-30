package com.anisanurjanah.asclepius.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anisanurjanah.asclepius.databinding.ItemSettingBinding
import com.anisanurjanah.asclepius.helper.Setting

class SettingAdapter(private val listSetting: ArrayList<Setting>) : RecyclerView.Adapter<SettingAdapter.ListViewHolder>() {
    class ListViewHolder(var binding: ItemSettingBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemSettingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = listSetting.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val menu = listSetting[position]
        holder.binding.tvSetting.text = menu.menu
    }
}