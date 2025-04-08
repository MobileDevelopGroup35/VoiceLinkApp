package com.l4kt.voicelink.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.l4kt.voicelink.data.database.dao.TaskDao
import com.l4kt.voicelink.data.database.entity.Task
import com.l4kt.voicelink.util.DateConverter

@Database(entities = [Task::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class VoiceLinkDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}