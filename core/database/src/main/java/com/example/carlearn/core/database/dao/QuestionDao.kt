package com.example.carlearn.core.database.dao

import androidx.room.*
import com.example.carlearn.core.database.entity.QuestionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QuestionEntity>)

    @Query("SELECT * FROM questions WHERE category = :category ORDER BY id ASC")
    fun getQuestionsByCategory(category: String): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE isFavorite = 1")
    fun getFavoriteQuestions(): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE wrongCount > 0 ORDER BY wrongCount DESC")
    fun getWrongQuestions(): Flow<List<QuestionEntity>>

    @Update
    suspend fun updateQuestion(question: QuestionEntity)

    @Query("SELECT COUNT(*) FROM questions WHERE category = :category")
    suspend fun getQuestionCount(category: String): Int
}