package com.l4kt.voicelink.data.repository

import com.l4kt.voicelink.data.database.dao.TaskDao
import com.l4kt.voicelink.data.database.entity.Task
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for managing tasks in the database.
 * This class exposes various methods for future feature development.
 */
@Singleton
class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
) {
    // Currently used methods

    fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()

    suspend fun insertTask(task: Task): Long = taskDao.insertTask(task)

    suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)

    suspend fun toggleTaskCompletion(task: Task) {
        taskDao.updateTask(task.copy(
            isCompleted = !task.isCompleted,
            updatedAt = Date()
        ))
    }

    // Methods reserved for future features (filtering, detailed views, etc.)
    // Suppressed to avoid warnings while preserving the API

    @Suppress("unused")
    fun getTasksByCategory(category: String): Flow<List<Task>> =
        taskDao.getTasksByCategory(category)

    @Suppress("unused")
    fun getTasksByCompletionStatus(isCompleted: Boolean): Flow<List<Task>> =
        taskDao.getTasksByCompletionStatus(isCompleted)

    @Suppress("unused")
    fun getUpcomingTasks(date: Date): Flow<List<Task>> =
        taskDao.getTasksDueBefore(date)

    @Suppress("unused")
    fun getAllCategories(): Flow<List<String>> = taskDao.getAllCategories()

    @Suppress("unused")
    suspend fun getTaskById(taskId: Long): Task? = taskDao.getTaskById(taskId)

    @Suppress("unused")
    suspend fun updateTask(task: Task) = taskDao.updateTask(task)
}