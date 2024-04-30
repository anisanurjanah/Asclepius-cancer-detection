package com.anisanurjanah.asclepius.view.activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.anisanurjanah.asclepius.R
import com.anisanurjanah.asclepius.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }

        val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
        val resultLabel = intent.getStringExtra(EXTRA_LABEL)
        val resultScore = intent.getStringExtra(EXTRA_SCORE)

        showLoading(true)
        binding.apply {
            if (imageUri != null && resultLabel != null && resultScore != null) {
                showLoading(false)
                setImage(imageUri)
                setResult(resultLabel, resultScore)
            } else {
                showLoading(false)
                binding.resultNotAvailable.visibility = View.VISIBLE
            }
        }
    }

    private fun setImage(result: Uri) {
        val uri = Uri.parse(result.toString())
        binding.resultImage.setImageURI(uri)
    }

    private fun setResult(resultLabel: String, resultScore: String) {
        binding.resultLabel.text = resultLabel
        binding.resultScore.text = resultScore

        if (resultLabel.equals("cancer", ignoreCase = true)) {
            binding.textResult.text = getString(R.string.text_cancer)
            binding.motivatedText.text = getString(R.string.motivated_text_cancer)
        } else {
            binding.textResult.text = getString(R.string.text_no_cancer)
            binding.motivatedText.text = getString(R.string.motivated_text_no_cancer)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_LABEL = "extra_label"
        const val EXTRA_SCORE = "extra_score"
    }
}