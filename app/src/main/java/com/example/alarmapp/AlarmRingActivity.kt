package com.example.alarmapp

import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.alarmapp.data.AlarmDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class AlarmRingActivity : AppCompatActivity() {
    private var ringtone: Ringtone? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_ring)

        val alarmId = intent.getIntExtra("ALARM_ID", -1)
        val timeTv = findViewById<TextView>(R.id.ring_time)
        val labelTv = findViewById<TextView>(R.id.ring_label)

        if (alarmId != -1) {
            CoroutineScope(Dispatchers.IO).launch {
                val db = AlarmDatabase.getDatabase(applicationContext)
                val alarm = db.alarmDao().getAlarmById(alarmId)
                launch(Dispatchers.Main) {
                    alarm?.let {
                        timeTv.text = it.formattedTime
                        labelTv.text = it.label
                    }
                }
            }
        }

        // Play default ringtone
        val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtone = RingtoneManager.getRingtone(applicationContext, notification)
        ringtone?.play()

        findViewById<Button>(R.id.btn_snooze).setOnClickListener {
            // Snooze: for now just stop and finish, in real app schedule again for +5-10 mins
            stopAndFinish()
        }

        findViewById<Button>(R.id.btn_dismiss).setOnClickListener {
            stopAndFinish()
        }
    }

    private fun stopAndFinish() {
        ringtone?.stop()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        ringtone?.stop()
    }
}
