package com.example.carlearn.core.database.repository

import com.example.carlearn.core.database.dao.QuestionDao
import com.example.carlearn.core.database.entity.QuestionEntity
import com.example.carlearn.core.database.network.QuestionApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuestionRepository @Inject constructor(
    private val questionDao: QuestionDao,
    private val questionApiService: QuestionApiService
) {
    fun getQuestionsByCategory(category: String): Flow<List<QuestionEntity>> {
        return questionDao.getQuestionsByCategory(category)
    }

    suspend fun syncQuestions() {
        try {
            val response = questionApiService.getQuestions()
            if (response.isSuccessful) {
                val networkQuestions = response.body()?.data ?: emptyList()
                val entities = networkQuestions.map { item ->
                    QuestionEntity(
                        id = item.id,
                        question = item.question,
                        optionA = item.options.getOrNull(0) ?: "",
                        optionB = item.options.getOrNull(1) ?: "",
                        optionC = item.options.getOrNull(2) ?: "",
                        optionD = item.options.getOrNull(3) ?: "",
                        answer = item.answer,
                        explanation = "来自服务器解析", // 后端模型中暂无解析字段，此处占位
                        category = "科目一", // 默认归类，后续可根据后端字段调整
                        type = if (item.options.size == 2) "判断题" else "单选题"
                    )
                }
                questionDao.insertQuestions(entities)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun updateQuestion(question: QuestionEntity) {
        questionDao.updateQuestion(question)
    }
}