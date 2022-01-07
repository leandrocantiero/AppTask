package com.example.tasks.service.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tasks.service.model.PriorityModel

@Dao
interface PriorityDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(list: List<PriorityModel>)

    @Query("select * from priority")
    fun list(): List<PriorityModel>

    @Query("select * from priority where id = :id")
    fun getById(id: Int): PriorityModel
}