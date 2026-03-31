package com.example.carlearn.core.database.network
import retrofit2.Response
import retrofit2.http.GET

data class QuestionItem(
    val id: Int,
    val question: String,
    val options: List<String>,
    val answer: String
)

data class QuestionsResponse(
    val success: Boolean,
    val data: List<QuestionItem>
)

interface QuestionApiService {

    @GET("api/questions")
    suspend fun getQuestions(): Response<QuestionsResponse>
}