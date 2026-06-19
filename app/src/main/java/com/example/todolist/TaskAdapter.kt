package com.example.todolist

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private val onTaskClick: (Task) -> Unit,
    private val onDeleteClick: (Task) -> Unit,
    private val onCompleteClick: (Task, Boolean) -> Unit
) : ListAdapter<Task, TaskAdapter.TaskViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task)
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.textViewTitle)
        private val description: TextView = itemView.findViewById(R.id.textViewDescription)
        private val priority: TextView = itemView.findViewById(R.id.textViewPriority)
        private val dueDate: TextView = itemView.findViewById(R.id.textViewDueDate)
        private val checkBoxDone: CheckBox = itemView.findViewById(R.id.checkBoxDone)
        private val buttonDelete: ImageButton = itemView.findViewById(R.id.buttonDelete)

        fun bind(task: Task) {
            title.text = task.title
            description.text = task.description
            description.visibility = if (task.description.isNullOrBlank()) View.GONE else View.VISIBLE
            
            val priorityText = when (task.priority) {
                1 -> "Low"
                2 -> "Medium"
                3 -> "High"
                else -> "None"
            }
            priority.text = "Priority: $priorityText"

            if (task.dueDate != null) {
                dueDate.text = "Due: ${DateFormat.format("yyyy-MM-dd", task.dueDate)}"
                dueDate.visibility = View.VISIBLE
            } else {
                dueDate.visibility = View.GONE
            }

            checkBoxDone.setOnCheckedChangeListener(null)
            checkBoxDone.isChecked = task.isCompleted
            checkBoxDone.setOnCheckedChangeListener { _, isChecked ->
                onCompleteClick(task, isChecked)
            }

            buttonDelete.setOnClickListener {
                onDeleteClick(task)
            }

            itemView.setOnClickListener {
                onTaskClick(task)
            }
        }
    }

    class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }
}
