package com.anisanurjanah.asclepius.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anisanurjanah.asclepius.data.local.entity.History
import com.anisanurjanah.asclepius.data.repository.Repository

class HistoryViewModel(private val repository: Repository) : ViewModel() {
    private val _favorite = MutableLiveData<Boolean>()

    fun getAllHistory() = repository.getAllHistory()

    fun insertHistory(history: History) {
        repository.insertHistory(history)
        _favorite.value = true
    }
}