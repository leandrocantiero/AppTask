package com.example.tasks.service.repository.remote

import com.example.tasks.service.model.HeaderModel
import com.example.tasks.service.model.TaskModel
import retrofit2.Call
import retrofit2.http.*

interface TaskService {
    @GET("Task")
    fun get(): Call<List<TaskModel>>

    @GET("Task/{id}")
    fun getById(@Path(value = "id", encoded = true) id: Int): Call<TaskModel>

    @GET("Task/Next7Days")
    fun getWeek(): Call<List<TaskModel>>

    @GET("Task/Overdue")
    fun overdue(): Call<List<TaskModel>>

    @POST("Task")
    @FormUrlEncoded
    fun create(
        @Field("Description") description: String,
        @Field("PriorityId") priorityId: Int,
        @Field("DueDate") dueDate: String,
        @Field("Complete") complete: Boolean
    ): Call<Boolean>

    @HTTP(method = "PUT", path = "Task", hasBody = true)
    @FormUrlEncoded
    fun update(
        @Field("Id") id: Int,
        @Field("Description") description: String,
        @Field("PriorityId") priorityId: Int,
        @Field("DueDate") dueDate: String,
        @Field("Complete") complete: Boolean
    ): Call<Boolean>

    @HTTP(method = "PUT", path = "Task/Complete", hasBody = true)
    @FormUrlEncoded
    fun complete(@Field("Id") id: Int): Call<Boolean>

    @HTTP(method = "PUT", path = "Task/Undo", hasBody = true)
    @FormUrlEncoded
    fun undo(@Field("Id") id: Int): Call<Boolean>

    @HTTP(method = "DELETE", path = "Task", hasBody = true)
    @FormUrlEncoded
    fun delete(@Field("Id") id: Int): Call<Boolean>
}