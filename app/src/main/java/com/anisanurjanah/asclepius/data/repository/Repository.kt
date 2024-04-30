package com.anisanurjanah.asclepius.data.repository

import androidx.lifecycle.LiveData
import com.anisanurjanah.asclepius.data.local.entity.History
import com.anisanurjanah.asclepius.data.local.room.HistoryDao
import com.anisanurjanah.asclepius.data.remote.retrofit.ApiService
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class Repository private constructor(
    private val apiService: ApiService,
    private val historyDao: HistoryDao,
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
) {
    fun getTopHeadlines(query: String, category: String, language: String) = apiService.getTopHeadlines(query, category, language)

    fun getAllHistory(): LiveData<List<History>> = historyDao.getAllHistory()
    fun insertHistory(history: History) {
        executorService.execute { historyDao.insertHistory(history) }
    }

    companion object {
        @Volatile
        private var INSTANCE: Repository? = null

        fun getInstance(apiService: ApiService, historyDao: HistoryDao): Repository = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Repository(apiService, historyDao)
        }.also { INSTANCE = it }
    }
}