package com.example.tasks.service.listener

import com.example.tasks.service.model.HeaderModel
import com.example.tasks.service.model.TaskModel

interface ApiListener<T> {

    fun onSuccess(model: T)

    fun onFailure(message: String)
}