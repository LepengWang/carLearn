package com.example.carlearn.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val phone: String,
    val password: String,
    val username: String = "智驾学员",
    val avatar: String? = null,
    val createTime: Long = System.currentTimeMillis()
)