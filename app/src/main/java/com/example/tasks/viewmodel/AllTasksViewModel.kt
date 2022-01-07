package com.example.tasks.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.ApiListener
import com.example.tasks.service.listener.ValidationListener
import com.example.tasks.service.model.PriorityModel
import com.example.tasks.service.model.TaskModel
import com.example.tasks.service.repository.TaskRepository

class AllTasksViewModel(application: Application) : AndroidViewModel(application) {
    private val mTaskRepository = TaskRepository(application)

    private val mTaskList = MutableLiveData<List<TaskModel>>()
    var taskList: LiveData<List<TaskModel>> = mTaskList

    private val mOperation = MutableLiveData<ValidationListener>()
    var operation: LiveData<ValidationListener> = mOperation

    fun get(mTaskFilter: Int) {
        val listener = object : ApiListener<List<TaskModel>> {
            override fun onSuccess(model: List<TaskModel>) {
                mTaskList.value = model
            }

            override fun onFailure(message: String) {
                mTaskList.value = arrayListOf()
                mOperation.value = ValidationListener(message)
            }
        }

        when (mTaskFilter) {
            TaskConstants.FILTER.ALL -> {
                mTaskRepository.get(listener)
            }

            TaskConstants.FILTER.NEXT -> {
                mTaskRepository.getWeek(listener)
            }

            TaskConstants.FILTER.EXPIRED -> {
                mTaskRepository.overdue(listener)
            }
        }

    }

    fun complete(id: Int) {
        updateStatus(id, true)
    }

    fun undo(id: Int) {
        updateStatus(id, false)
    }

    private fun updateStatus(id: Int, status: Boolean) {
        if (status) {
            mTaskRepository.complete(id, object : ApiListener<Boolean> {
                override fun onSuccess(model: Boolean) {
                    mOperation.value = ValidationListener()
                }

                override fun onFailure(message: String) {
                    mOperation.value = ValidationListener(message)
                }
            })
        } else {
            mTaskRepository.undo(id, object : ApiListener<Boolean> {
                override fun onSuccess(model: Boolean) {
                    mOperation.value = ValidationListener()
                }

                override fun onFailure(message: String) {
                    mOperation.value = ValidationListener(message)
                }
            })
        }
    }

    fun delete(id: Int) {
        mTaskRepository.delete(id, object : ApiListener<Boolean> {
            override fun onSuccess(model: Boolean) {
                mOperation.value = ValidationListener()
            }

            override fun onFailure(message: String) {
                mOperation.value = ValidationListener(message)
            }
        })
    }
}