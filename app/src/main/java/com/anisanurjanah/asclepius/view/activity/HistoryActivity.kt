package com.anisanurjanah.asclepius.view.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.anisanurjanah.asclepius.data.local.entity.History
import com.anisanurjanah.asclepius.databinding.ActivityHistoryBinding
import com.anisanurjanah.asclepius.helper.ViewModelFactory
import com.anisanurjanah.asclepius.model.HistoryViewModel
import com.anisanurjanah.asclepius.view.adapter.HistoryAdapter

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding

    private lateinit var historyViewModel: HistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }

        showLoading(true)
        historyViewModel = obtainViewModel(this@HistoryActivity)
        historyViewModel.getAllHistory().observe(this@HistoryActivity) {
            setHistory(it)
            showLoading(false)
        }
    }

    private fun setHistory(history: List<History>) {
        val layoutManager = LinearLayoutManager(this@HistoryActivity)
        binding.rvHistory.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this@HistoryActivity, layoutManager.orientation)
        binding.rvHistory.addItemDecoration(itemDecoration)

        val historyAdapter = HistoryAdapter(history)

        if (history.isEmpty()) {
            binding.rvHistory.visibility = View.GONE
            binding.historyNotAvailable.visibility = View.VISIBLE
        } else {
            binding.rvHistory.visibility = View.VISIBLE
            binding.historyNotAvailable.visibility = View.GONE

            binding.rvHistory.adapter = historyAdapter
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun obtainViewModel(activity: AppCompatActivity): HistoryViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[HistoryViewModel::class.java]
    }
}