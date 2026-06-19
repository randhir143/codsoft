package com.example.todolist

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val taskViewModel: TaskViewModel by viewModels()
    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewTasks)
        adapter = TaskAdapter(
            onTaskClick = { task -> showTaskDialog(task) },
            onDeleteClick = { task -> taskViewModel.delete(task) },
            onCompleteClick = { task, isChecked ->
                taskViewModel.update(task.copy(isCompleted = isChecked))
            }
        )
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        taskViewModel.allTasks.observe(this) { tasks ->
            adapter.submitList(tasks)
        }

        findViewById<FloatingActionButton>(R.id.fabAddTask).setOnClickListener {
            showTaskDialog()
        }
    }

    private fun showTaskDialog(task: Task? = null) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_task, null)
        val editTitle = dialogView.findViewById<EditText>(R.id.editTextTitle)
        val editDescription = dialogView.findViewById<EditText>(R.id.editTextDescription)
        val radioGroupPriority = dialogView.findViewById<RadioGroup>(R.id.radioGroupPriority)
        val buttonPickDate = dialogView.findViewById<Button>(R.id.buttonPickDate)
        val textSelectedDate = dialogView.findViewById<TextView>(R.id.textViewSelectedDate)

        var selectedDate: Long? = task?.dueDate
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        if (task != null) {
            editTitle.setText(task.title)
            editDescription.setText(task.description)
            when (task.priority) {
                1 -> radioGroupPriority.check(R.id.radioLow)
                2 -> radioGroupPriority.check(R.id.radioMedium)
                3 -> radioGroupPriority.check(R.id.radioHigh)
            }
            selectedDate?.let {
                textSelectedDate.text = sdf.format(Date(it))
            }
        }

        buttonPickDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            selectedDate?.let { calendar.timeInMillis = it }
            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val selectedCalendar = Calendar.getInstance()
                    selectedCalendar.set(year, month, dayOfMonth)
                    selectedDate = selectedCalendar.timeInMillis
                    textSelectedDate.text = sdf.format(selectedCalendar.time)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        AlertDialog.Builder(this)
            .setTitle(if (task == null) "Add Task" else "Edit Task")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val title = editTitle.text.toString()
                if (title.isBlank()) {
                    Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val description = editDescription.text.toString()
                val priority = when (radioGroupPriority.checkedRadioButtonId) {
                    R.id.radioLow -> 1
                    R.id.radioMedium -> 2
                    R.id.radioHigh -> 3
                    else -> 2
                }

                if (task == null) {
                    taskViewModel.insert(Task(title = title, description = description, priority = priority, dueDate = selectedDate))
                } else {
                    taskViewModel.update(task.copy(title = title, description = description, priority = priority, dueDate = selectedDate))
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
