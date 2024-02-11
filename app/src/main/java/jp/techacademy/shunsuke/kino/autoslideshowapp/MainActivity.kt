package jp.techacademy.shunsuke.kino.autoslideshowapp

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import jp.techacademy.shunsuke.kino.autoslideshowapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val imageList: MutableList<Uri> = mutableListOf()
    private var currentIndex = 0
    private var isPlaying = false
    private val handler = Handler(Looper.getMainLooper())
    private val delayMillis: Long = 2000 // 2秒ごとに画像を送る

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // エミュレーター内の画像ファイルのURIを取得
        val imageFolders = listOf(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        )

        imageFolders.forEach { folder ->
            folder?.listFiles()?.forEach { file ->
                val imageUri = Uri.fromFile(file)
                imageList.add(imageUri)
            }
        }

        // 進むボタン
        binding.buttonNext.setOnClickListener {
            showNextImage()
        }

        // 戻るボタン
        binding.buttonPrevious.setOnClickListener {
            showPreviousImage()
        }

        // 再生/停止ボタン
        binding.buttonPlayPause.setOnClickListener {
            togglePlayPause()
        }

        // 初期画像を表示
        showImage(currentIndex)
    }

    private fun showImage(index: Int) {
        if (index >= 0 && index < imageList.size) {
            binding.imageView.setImageURI(imageList[index])
        }
    }

    private fun showNextImage() {
        currentIndex = (currentIndex + 1) % imageList.size
        showImage(currentIndex)
    }

    private fun showPreviousImage() {
        currentIndex = (currentIndex - 1 + imageList.size) % imageList.size
        showImage(currentIndex)
    }

    private fun togglePlayPause() {
        isPlaying = !isPlaying
        if (isPlaying) {
            binding.buttonPlayPause.text = "■ STOP"
            startSlideshow()
        } else {
            binding.buttonPlayPause.text = "▶ PLAY"
            stopSlideshow()
        }
    }

    private fun startSlideshow() {
        handler.postDelayed(slideshowRunnable, delayMillis)
    }

    private fun stopSlideshow() {
        handler.removeCallbacks(slideshowRunnable)
    }

    private val slideshowRunnable = object : Runnable {
        override fun run() {
            showNextImage()
            if (isPlaying) {
                handler.postDelayed(this, delayMillis)
            }
        }
    }
}


