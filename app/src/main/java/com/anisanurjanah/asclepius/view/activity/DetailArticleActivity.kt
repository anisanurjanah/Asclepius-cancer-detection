package com.anisanurjanah.asclepius.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.anisanurjanah.asclepius.R
import com.anisanurjanah.asclepius.data.remote.response.ArticlesItem
import com.anisanurjanah.asclepius.databinding.ActivityDetailArticleBinding
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class DetailArticleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailArticleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        val articlesItem = intent.getParcelableExtra<ArticlesItem>(EXTRA_ARTICLE)

        binding.apply {
            topAppBar.title = articlesItem?.title
            topAppBar.setNavigationOnClickListener {
                finish()
            }

            topAppBar.inflateMenu(R.menu.share_menu)
            topAppBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_share -> {
                        if (articlesItem != null) {
                            shareArticle(articlesItem)
                        }
                        true
                    }
                    else -> false
                }
            }
        }

        if (articlesItem != null) {
            binding.progressIndicator.visibility = View.GONE
            setDetailArticle(articlesItem)
        } else {
            binding.progressIndicator.visibility = View.VISIBLE
        }
    }

    private fun setDetailArticle(article: ArticlesItem) {
        binding.apply {
            articleTitle.text = article.title ?: getString(R.string.no_title_available)
            articleSource.text = article.source?.name ?: getString(R.string.no_source_available)
            articleAuthor.text = article.author ?: getString(R.string.no_author_available)
            articleDescription.text = article.description ?: getString(R.string.no_description_available)
            articlePublished.text = parsingDate(article.publishedAt) ?: getString(R.string.no_published_date_available)
            articleContent.text = article.content ?: getString(R.string.no_content_available)
            articleUrl.text = article.url ?: getString(R.string.no_link_available)
            articleUrl.setOnClickListener {
                if (!article.url.isNullOrEmpty()) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
                    startActivity(intent)
                }
            }

            Glide.with(this@DetailArticleActivity)
                .load(article.urlToImage)
                .into(articleImage)
        }
    }

    private fun shareArticle(article: ArticlesItem) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"

        val shareMessage = StringBuilder()
        shareMessage.append("${article.title}\n")
        shareMessage.append("Tap link below to see article\n")
        shareMessage.append(article.url)

        intent.putExtra(Intent.EXTRA_TEXT, shareMessage.toString())
        startActivity(Intent.createChooser(intent, "Share article"))
    }

    private fun parsingDate(inputDateString: String?): String? {
        if (inputDateString.isNullOrEmpty()) {
            return null
        }

        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        val outputFormat = SimpleDateFormat("MMMM dd, yyyy, HH:mm", Locale.US)

        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        outputFormat.timeZone = TimeZone.getTimeZone("UTC")

        val date = inputFormat.parse(inputDateString)

        return outputFormat.format(date!!)
    }

    companion object {
        const val EXTRA_ARTICLE = "extra_article"
    }
}