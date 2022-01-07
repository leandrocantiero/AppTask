package com.example.tasks.service.repository

import android.content.Context
import com.example.tasks.R
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.ApiListener
import com.example.tasks.service.model.TaskModel
import com.example.tasks.service.repository.remote.RetrofitClient
import com.example.tasks.service.repository.remote.TaskService
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskRepository(context: Context) : BaseRepository(context) {
    private val mRemote = RetrofitClient.createService(TaskService::class.java)

    fun get(listener: ApiListener<List<TaskModel>>) {
        val call: Call<List<TaskModel>> = mRemote.get()
        list(call, listener)
    }

    fun getWeek(listener: ApiListener<List<TaskModel>>) {
        val call: Call<List<TaskModel>> = mRemote.getWeek()
        list(call, listener)
    }

    fun overdue(listener: ApiListener<List<TaskModel>>) {
        val call: Call<List<TaskModel>> = mRemote.overdue()
        list(call, listener)
    }

    private fun list(call: Call<List<TaskModel>>, listener: ApiListener<List<TaskModel>>) {
        if (!isConnectionAvailable(context)) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        call.enqueue(object : Callback<List<TaskModel>> {
            override fun onResponse(
                call: Call<List<TaskModel>>,
                res: Response<List<TaskModel>>
            ) {
                if (res.code() != TaskConstants.HTTP.SUCCESS) {
                    val message = Gson().fromJson(res.errorBody()!!.string(), String::class.java)

                    listener.onFailure(message)
                } else {
                    res.body()?.let { listener.onSuccess(it) }
                }
            }

            override fun onFailure(call: Call<List<TaskModel>>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }

        })
    }

    fun getById(id: Int, listener: ApiListener<TaskModel>) {
        val call: Call<TaskModel> = mRemote.getById(id)
        call.enqueue(object : Callback<TaskModel> {
            override fun onResponse(
                call: Call<TaskModel>,
                res: Response<TaskModel>
            ) {
                if (res.code() != TaskConstants.HTTP.SUCCESS) {
                    val message = Gson().fromJson(res.errorBody()!!.string(), String::class.java)

                    listener.onFailure(message)
                } else {
                    res.body()?.let { listener.onSuccess(it) }
                }
            }

            override fun onFailure(call: Call<TaskModel>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }

        })
    }

    fun create(
        description: String,
        priorityId: Int,
        dueDate: String,
        complete: Boolean,
        listener: ApiListener<Boolean>
    ) {
        if (!isConnectionAvailable(context)) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call: Call<Boolean> = mRemote.create(description, priorityId, dueDate, complete)
        call.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, res: Response<Boolean>) {
                if (res.code() != TaskConstants.HTTP.SUCCESS) {
                    val message = Gson().fromJson(res.errorBody()!!.string(), String::class.java)

                    listener.onFailure(message)
                } else {
                    res.body()?.let { listener.onSuccess(it) }
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }
        })
    }

    fun update(
        id: Int,
        description: String,
        priorityId: Int,
        dueDate: String,
        complete: Boolean,
        listener: ApiListener<Boolean>
    ) {
        if (!isConnectionAvailable(context)) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call: Call<Boolean> = mRemote.update(id, description, priorityId, dueDate, complete)
        call.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, res: Response<Boolean>) {
                if (res.code() != TaskConstants.HTTP.SUCCESS) {
                    val message = Gson().fromJson(res.errorBody()!!.string(), String::class.java)

                    listener.onFailure(message)
                } else {
                    res.body()?.let { listener.onSuccess(it) }
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }
        })
    }

    fun complete(id: Int, listener: ApiListener<Boolean>) {
        if (!isConnectionAvailable(context)) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call: Call<Boolean> = mRemote.complete(id)
        call.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, res: Response<Boolean>) {
                if (res.code() != TaskConstants.HTTP.SUCCESS) {
                    val message = Gson().fromJson(res.errorBody()!!.string(), String::class.java)

                    listener.onFailure(message)
                } else {
                    res.body()?.let { listener.onSuccess(it) }
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }
        })
    }

    fun undo(id: Int, listener: ApiListener<Boolean>) {
        if (!isConnectionAvailable(context)) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call: Call<Boolean> = mRemote.undo(id)
        call.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, res: Response<Boolean>) {
                if (res.code() != TaskConstants.HTTP.SUCCESS) {
                    val message = Gson().fromJson(res.errorBody()!!.string(), String::class.java)

                    listener.onFailure(message)
                } else {
                    res.body()?.let { listener.onSuccess(it) }
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }
        })
    }

    fun delete(id: Int, listener: ApiListener<Boolean>) {
        if (!isConnectionAvailable(context)) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call: Call<Boolean> = mRemote.delete(id)
        call.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, res: Response<Boolean>) {
                if (res.code() != TaskConstants.HTTP.SUCCESS) {
                    val message = Gson().fromJson(res.errorBody()!!.string(), String::class.java)

                    listener.onFailure(message)
                } else {
                    res.body()?.let { listener.onSuccess(it) }
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }
        })
    }
}