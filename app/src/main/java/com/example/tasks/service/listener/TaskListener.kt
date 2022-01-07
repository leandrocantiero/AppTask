package com.example.tasks.service.listener

import com.example.tasks.service.model.TaskModel

interface TaskListener {

    fun onListClick(id: Int)

    fun onDeleteClick(id: Int)

    fun onCompleteClick(id: Int)

    fun onUndoClick(id: Int)
}