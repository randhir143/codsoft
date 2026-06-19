package com.example.todolist

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val taskDao = TaskDatabase.getDatabase(application).taskDao()
    val allTasks: LiveData<List<Task>> = taskDao.getAllTasks().asLiveData()

    fun insert(task: Task) = viewModelScope.launch {
        taskDao.insert(task)
    }

    fun update(task: Task) = viewModelScope.launch {
        taskDao.update(task)
    }

    fun delete(task: Task) = viewModelScope.launch {
        taskDao.delete(task)
    }
}
