package com.anisanurjanah.asclepius.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArticleResponse(
	@field:SerializedName("articles")
	val articles: List<ArticlesItem?>? = null,
) : Parcelable

@Parcelize
data class ArticlesItem(

	@field:SerializedName("publishedAt")
	val publishedAt: String? = null,

	@field:SerializedName("author")
	val author: String? = null,

	@field:SerializedName("urlToImage")
	val urlToImage: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("source")
	val source: Source? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("url")
	val url: String? = null,

	@field:SerializedName("content")
	val content: String? = null,

	@field:SerializedName("isDeleted")
	var isDeleted: Boolean = false
) : Parcelable

@Parcelize
data class Source(
	@field:SerializedName("name")
	val name: String? = null,
) : Parcelable