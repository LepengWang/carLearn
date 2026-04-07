package com.example.carlearn.core.database.di

import android.content.Context
import androidx.room.Room
import com.example.carlearn.core.database.AppDatabase
import com.example.carlearn.core.database.dao.QuestionDao
import com.example.carlearn.core.database.dao.UserDao
import com.example.carlearn.core.database.network.QuestionApiService
import com.example.carlearn.core.database.network.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "car_learn_db"
        ).fallbackToDestructiveMigration()
         .build()
    }

    @Provides
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    fun provideQuestionDao(database: AppDatabase): QuestionDao {
        return database.questionDao()
    }

    @Provides
    @Singleton
    fun provideQuestionApiService(): QuestionApiService {
        return RetrofitClient.questionApi
    }
}