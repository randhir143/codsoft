package com.example.alarmapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.alarmapp.R
import com.example.alarmapp.model.Alarm
import com.google.android.material.switchmaterial.SwitchMaterial

class AlarmAdapter(
    private val onToggle: (Alarm) -> Unit,
    private val onDelete: (Alarm) -> Unit
) : ListAdapter<Alarm, AlarmAdapter.AlarmViewHolder>(AlarmDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_alarm, parent, false)
        return AlarmViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val alarm = getItem(position)
        holder.bind(alarm, onToggle, onDelete)
    }

    class AlarmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTime: TextView = itemView.findViewById(R.id.tv_time)
        private val tvLabel: TextView = itemView.findViewById(R.id.tv_label)
        private val switchAlarm: SwitchMaterial = itemView.findViewById(R.id.switch_alarm)

        fun bind(alarm: Alarm, onToggle: (Alarm) -> Unit, onDelete: (Alarm) -> Unit) {
            tvTime.text = alarm.formattedTime
            tvLabel.text = alarm.label
            switchAlarm.isChecked = alarm.isEnabled

            switchAlarm.setOnCheckedChangeListener { _, isChecked ->
                if (alarm.isEnabled != isChecked) {
                    alarm.isEnabled = isChecked
                    onToggle(alarm)
                }
            }

            itemView.setOnLongClickListener {
                onDelete(alarm)
                true
            }
        }
    }

    class AlarmDiffCallback : DiffUtil.ItemCallback<Alarm>() {
        override fun areItemsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
            return oldItem == newItem
        }
    }
}
