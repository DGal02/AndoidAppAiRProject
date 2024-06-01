package com.example.projectair

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class glider : AppCompatActivity() {
    private val url: String = "https://picsum.photos/300/300"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_glider)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val buttonReturn = findViewById<Button>(R.id.goBackGlider)
        val buttonChange = findViewById<Button>(R.id.changeImgGlider)
        val imageViewUI = findViewById<ImageView>(R.id.imageView)
        Glide.with(this)
            .load(url)
            .fitCenter()
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .placeholder(R.drawable.image300)
            .into(imageViewUI)
        buttonReturn.setOnClickListener {
            val intent = Intent(this, clock::class.java)
            startActivity(intent)
            finish()
        }
        buttonChange.setOnClickListener {
            Glide.with(this)
                .load(url)
                .fitCenter()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.image300)
                .into(imageViewUI)
        }
    }
}