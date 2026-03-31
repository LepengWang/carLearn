package com.example.carlearn.core.database.network
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


data class LoginRequest(
    val phone: String,
    val password: String
)

data class RegisterRequest(
    val phone: String,
    val password: String
)

data class AuthResponse(
    val success: Boolean,
    val message: String,
    val phone: String? = null
)

interface AuthApiService {

    @POST("api/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<AuthResponse>

    @POST("api/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<AuthResponse>
}