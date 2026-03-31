package com.example.carlearn.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.carlearn.core.database.dao.QuestionDao
import com.example.carlearn.core.database.dao.UserDao
import com.example.carlearn.core.database.entity.QuestionEntity
import com.example.carlearn.core.database.entity.UserEntity

@Database(entities = [UserEntity::class, QuestionEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun questionDao(): QuestionDao
}