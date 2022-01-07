package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.listener.ApiListener
import com.example.tasks.service.listener.ValidationListener
import com.example.tasks.service.model.PriorityModel
import com.example.tasks.service.model.TaskModel
import com.example.tasks.service.repository.PriorityRepository
import com.example.tasks.service.repository.TaskRepository

class TaskFormViewModel(application: Application) : AndroidViewModel(application) {

    private val mTaskRepository = TaskRepository(application)
    private val mPriorityRepository = PriorityRepository(application)

    private val mValidation = MutableLiveData<ValidationListener>()
    var validation: LiveData<ValidationListener> = mValidation

    private val mLoadTask = MutableLiveData<TaskModel>()
    var loadTask: LiveData<TaskModel> = mLoadTask

    private val mPriorityList = MutableLiveData<List<PriorityModel>>()
    var priorityList: LiveData<List<PriorityModel>> = mPriorityList

    fun save(
        id: Int,
        description: String,
        priorityId: Int,
        dueDate: String,
        complete: Boolean
    ) {
        if (id > 0) {
            mTaskRepository.update(
                id,
                description,
                priorityId,
                dueDate,
                complete,
                object : ApiListener<Boolean> {
                    override fun onSuccess(model: Boolean) {
                        mValidation.value = ValidationListener()
                    }

                    override fun onFailure(message: String) {
                        mValidation.value = ValidationListener(message)
                    }

                }
            )
        } else {
            mTaskRepository.create(
                description,
                priorityId,
                dueDate,
                complete,
                object : ApiListener<Boolean> {
                    override fun onSuccess(model: Boolean) {
                        mValidation.value = ValidationListener()
                    }

                    override fun onFailure(message: String) {
                        mValidation.value = ValidationListener(message)
                    }

                }
            )
        }
    }

    fun loadPriorities() {
        mPriorityList.value = mPriorityRepository.list()
    }

    fun load(id: Int) {
        mTaskRepository.getById(id, object : ApiListener<TaskModel> {
            override fun onSuccess(model: TaskModel) {
                mLoadTask.value = model
            }

            override fun onFailure(message: String) {
                mLoadTask.value = null
            }
        })
    }
}