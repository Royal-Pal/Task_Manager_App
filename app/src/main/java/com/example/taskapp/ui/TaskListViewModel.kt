package com.example.taskapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.taskapp.data.SortColumn
import com.example.taskapp.data.Task
import com.example.taskapp.data.TaskListRepository

class TaskListViewModel(application: Application) : AndroidViewModel(application) {

    private val repo: TaskListRepository = TaskListRepository(application)

    private val _sortOrder = MutableLiveData(SortColumn.Priority)

    val tasks: LiveData<List<Task>> = Transformations.switchMap(_sortOrder) {
        repo.getTasks(_sortOrder.value!!)
    }

    fun changeSortOrder(newSortOrder: SortColumn) {
        _sortOrder.value = newSortOrder
    }
}