package com.example.sayacapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.LinearLayout
import android.widget.TextView
import android.os.Handler
import android.os.Looper
import java.time.ZonedDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DigitalClockActivity : AppCompatActivity() {

    private lateinit var clockContainer: LinearLayout
    private val handler = Handler(Looper.getMainLooper())
    private val timeZones = listOf(
        "Europe/Istanbul" to "Istanbul",
        "Asia/Tokyo" to "Tokyo",
        "America/New_York" to "New York",
        "Europe/London" to "London",
        "Asia/Dubai" to "Dubai",
        "Australia/Sydney" to "Sydney"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_digital_clock)

        clockContainer = findViewById(R.id.clockContainer)

        // Create clock displays for each timezone
        for ((zoneId, zoneName) in timeZones) {
            val clockView = createClockView(zoneId, zoneName)
            clockContainer.addView(clockView)
        }

        // Update time every second
        updateTime()
    }

    private fun createClockView(zoneId: String, zoneName: String): LinearLayout {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(16, 16, 16, 16)
            }
            setBackgroundResource(R.drawable.clock_background)
            setPadding(16, 16, 16, 16)
        }

        // Zone Name
        val nameView = TextView(this).apply {
            text = zoneName
            textSize = 14f
            setTextColor(getColor(R.color.primary))
        }

        // Time Display
        val timeView = TextView(this).apply {
            tag = zoneId
            textSize = 32f
            setTextColor(getColor(R.color.black))
            typeface = android.graphics.Typeface.MONOSPACE
        }

        // Date Display
        val dateView = TextView(this).apply {
            tag = "date_$zoneId"
            textSize = 12f
            setTextColor(getColor(R.color.secondary))
        }

        layout.addView(nameView)
        layout.addView(timeView)
        layout.addView(dateView)

        return layout
    }

    private fun updateTime() {
        for ((zoneId, _) in timeZones) {
            val zoneTime = ZonedDateTime.now(ZoneId.of(zoneId))

            // Format time
            val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
            val timeString = zoneTime.format(timeFormatter)

            // Format date
            val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
            val dateString = zoneTime.format(dateFormatter)

            // Update UI
            clockContainer.findViewWithTag<TextView>(zoneId)?.text = timeString
            clockContainer.findViewWithTag<TextView>("date_$zoneId")?.text = dateString
        }

        // Schedule next update
        handler.postDelayed({ updateTime() }, 1000)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}
