package com.github.abdularis.gaugeviewsample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.github.abdularis.gaugeview.LinearGaugeView
import java.util.*

class MainActivity : AppCompatActivity() {

    val rand : Random = Random(System.currentTimeMillis())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onRandomClick(view : View) {
        val signalBar = findViewById<LinearGaugeView>(R.id.gauge_view)
        val textView = findViewById<TextView>(R.id.text_view)
        signalBar.currentNumber = rand.nextInt(signalBar.maxNumber)
        textView.text = signalBar.currentNumber.toString()
    }
}
