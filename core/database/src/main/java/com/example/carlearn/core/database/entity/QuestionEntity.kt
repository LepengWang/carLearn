package com.example.carlearn.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
data class QuestionEntity(
    @PrimaryKey val id: Int,
    val question: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val answer: String, // A, B, C, D
    val explanation: String,
    val category: String, // 科目一, 科目四
    val type: String, // 判断题, 单选题, 多选题
    val isFavorite: Boolean = false,
    val wrongCount: Int = 0
)