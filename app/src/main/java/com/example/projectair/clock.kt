package com.example.projectair

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class clock : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_clock)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val buttonGoBack = findViewById<Button>(R.id.buttonGoBack)
        val buttonGyro = findViewById<Button>(R.id.info)
        val buttonGlider = findViewById<Button>(R.id.goToGlider)
        buttonGoBack.setOnClickListener {
            val intent = Intent(this, MainApp::class.java)
            startActivity(intent)
            finish()
        }
        buttonGyro.setOnClickListener {
            val intent = Intent(this, gyro::class.java)
            startActivity(intent)
            finish()
        }
        buttonGlider.setOnClickListener {
            val intent = Intent(this, glider::class.java)
            startActivity(intent)
            finish()
        }
    }
}