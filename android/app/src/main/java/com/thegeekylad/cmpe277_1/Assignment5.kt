package com.thegeekylad.cmpe277_1

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType

class Assignment5 : ComponentActivity(), SensorEventListener {

    lateinit var sensorManager: SensorManager
    lateinit var sensorProximity: Sensor
    lateinit var mediaPlayer: MediaPlayer

    val valueProximity = mutableStateOf(0f)

    @OptIn(ExperimentalUnitApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "Proximity Reading",
                    fontSize = TextUnit(20f, TextUnitType.Sp)
                )
                Text(
                    valueProximity.value.toString(),
                    fontSize = TextUnit(40f, TextUnitType.Sp)
                )
            }
        }

        // sensor stuff
        val sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val sensorProximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        sensorManager.registerListener(
            this,
            sensorProximity,
            SensorManager.SENSOR_DELAY_NORMAL
        )

        // media player stuff
        mediaPlayer = MediaPlayer.create(applicationContext, R.raw.clap)
    }

    override fun onPause() {
        super.onPause()

        sensorManager.unregisterListener(this)
        mediaPlayer.release()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        valueProximity.value = event!!.values[0]

        // "clap!"
        if (event.values[0] == 0f)
            mediaPlayer.start()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}