package com.anisanurjanah.asclepius.di

import android.content.Context
import com.anisanurjanah.asclepius.data.local.room.HistoryRoomDatabase
import com.anisanurjanah.asclepius.data.remote.retrofit.ApiConfig
import com.anisanurjanah.asclepius.data.repository.Repository

object Injection {
    fun provideRepository(context: Context): Repository {
        val apiService = ApiConfig.getApiService()

        val database = HistoryRoomDatabase.getDatabase(context)
        val historyDao = database.historyDao()

        return Repository.getInstance(apiService, historyDao)
    }
}