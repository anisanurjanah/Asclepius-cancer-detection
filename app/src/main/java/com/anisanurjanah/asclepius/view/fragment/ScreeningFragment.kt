package com.anisanurjanah.asclepius.view.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.anisanurjanah.asclepius.R
import com.anisanurjanah.asclepius.data.local.entity.History
import com.anisanurjanah.asclepius.databinding.FragmentScreeningBinding
import com.anisanurjanah.asclepius.helper.ImageClassifierHelper
import com.anisanurjanah.asclepius.helper.ViewModelFactory
import com.anisanurjanah.asclepius.model.HistoryViewModel
import com.anisanurjanah.asclepius.view.activity.HistoryActivity
import com.anisanurjanah.asclepius.view.activity.ResultActivity
import com.yalantis.ucrop.UCrop
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File
import java.text.NumberFormat
import java.util.Calendar

class ScreeningFragment : Fragment() {
    private var _binding: FragmentScreeningBinding? = null

    private val binding get() = _binding!!

    private lateinit var historyViewModel: HistoryViewModel

    private var currentImageUri: Uri? = null
    private var label: String? = null
    private var score: String? = null

    private lateinit var imageClassifierHelper: ImageClassifierHelper

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (!isGranted) {
                showToast("Notifications permission rejected")
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentScreeningBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        historyViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory.getInstance(requireActivity())
        )[HistoryViewModel::class.java]

        binding.apply {
            galleryButton.setOnClickListener { startGallery() }
            cameraButton.setOnClickListener { startCamera() }
            historyButton.setOnClickListener { moveToHistory() }
            cropButton.setOnClickListener {
                croppedImage()
            }
            analyzeButton.setOnClickListener {
                if (currentImageUri != null) {
                    analyzeImage()
                    analysisSuccess()
                    sendNotification()
                }  else {
                    showToast(getString(R.string.image_not_choose))
                }
            }
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCamera() {
        showToast(getString(R.string.camera_feature_failed))
    }

    private fun moveToHistory() {
        val intent = Intent(requireActivity(), HistoryActivity::class.java)
        startActivity(intent)
    }

    private fun analyzeImage() {
        imageClassifierHelper = ImageClassifierHelper(
            context = requireActivity(),
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    Toast.makeText(requireActivity(), error, Toast.LENGTH_SHORT).show()
                }

                override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                    results?.let { it ->
                        if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                            val result = it[0].categories[0]
                            label = result.label
                            score = NumberFormat.getPercentInstance().format(result.score)
                        }
                        moveToResult()
                    }
                }
            }
        )
        currentImageUri?.let {
            currentImageUri = it
            imageClassifierHelper.classifyStaticImage(it)
        }
    }

    private fun moveToResult() {
        val intent = Intent(requireActivity(), ResultActivity::class.java)
        intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, currentImageUri.toString())
        intent.putExtra(ResultActivity.EXTRA_LABEL, label)
        intent.putExtra(ResultActivity.EXTRA_SCORE, score)
        startActivity(intent)
    }

    private fun showImage() {
        binding.previewImageView.setImageDrawable(null)
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun croppedImage() {
        if (currentImageUri != null) {
            currentImageUri?.let {
                Log.d("Image URI", "showImageCropped: $it")
                startCropActivity(it)
            }
        }  else {
            showToast(getString(R.string.image_not_choose_crop_button))
        }
    }

    private fun startCropActivity(uri: Uri) {
        val destinationUri = Uri.fromFile(File(requireContext().cacheDir, "cropped_image.jpg"))

        UCrop.of(uri, destinationUri)
            .withAspectRatio(16F, 9F)
            .withMaxResultSize(200, 200)
            .start(requireActivity(), this)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    private fun generateDateTime(): String {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        return "$year-$month-$day $hour:$minute"
    }

    private fun analysisSuccess() {
        val dateTime = generateDateTime()
        val result = History(
            label = label.toString(),
            imageUri = currentImageUri.toString(),
            score = score,
            timestamp = dateTime
        )

        Log.d("ScreeningFragment", "Analysis success, data to be inserted: $result")

        historyViewModel.insertHistory(result)
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun sendNotification() {
        val intent = Intent(requireActivity(), HistoryActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            requireActivity(),
            0,
            intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)

        val notificationManager = getSystemService(requireContext(), NotificationManager::class.java)
        val builder = NotificationCompat.Builder(requireActivity(), CHANNEL_ID)
            .setContentTitle(getString(R.string.notifications_title))
            .setSmallIcon(R.drawable.baseline_notifications_active_24)
            .setContentText(getString(R.string.notifications_message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            builder.setChannelId(CHANNEL_ID)
            notificationManager?.createNotificationChannel(channel)
        }
        val notification = builder.build()
        notificationManager?.notify(NOTIFICATION_ID, notification)
    }

    private fun setToolbar() {
        val toolbar = requireActivity().findViewById<Toolbar>(R.id.topAppBar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        toolbar.title = getString(R.string.screening)
        toolbar.setTitleTextAppearance(requireContext(), R.style.ToolbarTitleText)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            currentImageUri = UCrop.getOutput(data!!)
            if (currentImageUri != null) {
                Log.d("Crop", "Cropped image URI: $currentImageUri")
                binding.previewImageView.setImageURI(currentImageUri)
                showImage()
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
            Log.e("Crop", "Error while cropping: $cropError")
            showToast("$cropError")
        }
    }

    override fun onResume() {
        super.onResume()
        setToolbar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "channel_05"
        private const val CHANNEL_NAME = "anisa nurjanah channel"
    }
}