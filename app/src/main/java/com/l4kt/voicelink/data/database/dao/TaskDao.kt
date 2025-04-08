package com.l4kt.voicelink.data.database.dao

import androidx.room.*
import com.l4kt.voicelink.data.database.entity.Task
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface TaskDao {
    @Insert
    suspend fun insertTask(task: Task): Long

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM tasks ORDER BY createdAt DESC")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskById(taskId: Long): Task?

    @Query("SELECT * FROM tasks WHERE category = :category ORDER BY createdAt DESC")
    fun getTasksByCategory(category: String): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE isCompleted = :isCompleted ORDER BY createdAt DESC")
    fun getTasksByCompletionStatus(isCompleted: Boolean): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE dueDate <= :date AND isCompleted = 0 ORDER BY dueDate ASC")
    fun getTasksDueBefore(date: Date): Flow<List<Task>>

    @Query("SELECT DISTINCT category FROM tasks")
    fun getAllCategories(): Flow<List<String>>
}