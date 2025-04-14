package com.l4kt.voicelink.di

import android.content.Context
import androidx.room.Room
import com.l4kt.voicelink.data.database.VoiceLinkDatabase
import com.l4kt.voicelink.data.database.dao.TaskDao
import com.l4kt.voicelink.data.repository.TaskRepository
import com.l4kt.voicelink.util.SpeechRecognizer
import com.l4kt.voicelink.util.TaskAnalyzer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
@Suppress("unused")
object AppModule {

    @Provides
    @Singleton
    fun provideVoiceLinkDatabase(
        @ApplicationContext context: Context
    ): VoiceLinkDatabase {
        return Room.databaseBuilder(
            context,
            VoiceLinkDatabase::class.java,
            "voicelink.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideTaskDao(database: VoiceLinkDatabase): TaskDao {
        return database.taskDao()
    }

    @Provides
    @Singleton
    fun provideTaskRepository(taskDao: TaskDao): TaskRepository {
        return TaskRepository(taskDao)
    }

    @Provides
    @Singleton
    fun provideSpeechRecognizer(
        @ApplicationContext context: Context
    ): SpeechRecognizer {
        return SpeechRecognizer(context)
    }

    @Provides
    @Singleton
    fun provideTaskAnalyzer(): TaskAnalyzer {
        return TaskAnalyzer()
    }
}