package com.example.projectair

import android.content.Intent
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.abs

class gyro : AppCompatActivity(), SensorEventListener {
//    private lateinit var sensorManager: SensorManager
    private lateinit var square: TextView
    private lateinit var gyroText: TextView
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var gyroscopeSensor: Sensor? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gyro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        square = findViewById(R.id.tvSquare)
        gyroText = findViewById(R.id.gyroInfo)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

        val buttonGoBack = findViewById<Button>(R.id.goBackGyro)
        buttonGoBack.setOnClickListener {
            val intent = Intent(this, clock::class.java)
            startActivity(intent)
            finish()
        }

    }
    override fun onPause() {
        super.onPause()
        // Unregister the sensor listener to save power
        sensorManager.unregisterListener(this)
    }
    override fun onResume() {
        super.onResume()
        // Register the sensor listener
        gyroscopeSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
        }
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val sides = event.values[0]
            val upDown = event.values[1]
            square.apply {
                rotationX = upDown * -3f
                rotationY = sides * 3f
                rotation = -sides
                translationX = sides * -10
                translationY = upDown * 10
            }
            val color = if (upDown.toInt() == 0 && sides.toInt() == 0) Color.GREEN else Color.RED
            square.setBackgroundColor(color)

            square.text = "up/down ${upDown.toInt()}\nleft/right ${sides.toInt()}"

        }

        if (event?.sensor?.type == Sensor.TYPE_GYROSCOPE) {
            val angularSpeedX = event.values[0]
            val angularSpeedY = event.values[1]
            val angularSpeedZ = event.values[2]
            gyroText.text = String.format("X: %.2f Y: %.2f Z: %.2f", angularSpeedX, angularSpeedY, angularSpeedZ)
            val color = if (abs(angularSpeedX) <= 0.2 && abs(angularSpeedY) <= 0.2 && abs(angularSpeedZ) <= 0.2) Color.GREEN else Color.CYAN
            gyroText.setBackgroundColor(color)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        return
    }

    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        super.onDestroy()
    }
}