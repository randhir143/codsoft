package com.example.alarmapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class Alarm(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val hour: Int,
    val minute: Int,
    var isEnabled: Boolean = true,
    var label: String = "",
    var days: String = "" // Could be comma separated days or bitmask
) {
    val formattedTime: String
        get() {
            val h = if (hour == 0 || hour == 12) 12 else hour % 12
            val amPm = if (hour < 12) "AM" else "PM"
            return String.format("%02d:%02d %s", h, minute, amPm)
        }
}
