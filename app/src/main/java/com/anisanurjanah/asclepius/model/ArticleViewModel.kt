package com.anisanurjanah.asclepius.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anisanurjanah.asclepius.data.remote.response.ArticleResponse
import com.anisanurjanah.asclepius.data.remote.response.ArticlesItem
import com.anisanurjanah.asclepius.data.repository.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArticleViewModel(private val repository: Repository) : ViewModel() {

    private val _articles = MutableLiveData<List<ArticlesItem>>()
    val articles: LiveData<List<ArticlesItem>> = _articles

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        getArticles()
    }

    private fun getArticles() {
        _isLoading.value = true
        val client = repository.getTopHeadlines(QUERY, CATEGORY, LANGUAGE)
        client.enqueue(object : Callback<ArticleResponse> {
            override fun onResponse(
                call: Call<ArticleResponse>,
                response: Response<ArticleResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _articles.value = response.body()?.articles?.filterNotNull() ?: emptyList()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ArticleResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object{
        private const val TAG = "ArticleViewModel"

        private const val QUERY = "cancer"
        private const val CATEGORY = "health"
        private const val LANGUAGE = "en"
    }
}