package com.example.carlearn.feature.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.carlearn.core.database.UserPreferences
import com.example.carlearn.core.database.dao.UserDao
import com.example.carlearn.core.database.entity.UserEntity
import com.example.carlearn.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import com.example.carlearn.core.database.network.LoginRequest
import com.example.carlearn.core.database.network.RetrofitClient
import com.example.carlearn.core.database.network.AuthResponse
import retrofit2.Response

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userDao: UserDao,
    private val userPreferences: UserPreferences
) : BaseViewModel() {

    private val _loginResult = MutableLiveData<Result<UserEntity>>()
    val loginResult: LiveData<Result<UserEntity>> = _loginResult

    private val _registerResult = MutableLiveData<Result<UserEntity>>()
    val registerResult: LiveData<Result<UserEntity>> = _registerResult

    fun login(phone: String, psw: String) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.authApi.login(
                        LoginRequest(phone = phone, password = psw)
                    )
                }

                if (response.isSuccessful && response.body()?.success == true) {
                    userPreferences.setLoggedIn(true)
                    userPreferences.saveUserPhone(phone)
                    _loginResult.postValue(Result.success(UserEntity(phone = phone, password = psw)))
                } else {
                    val message = response.body()?.message ?: "账号或密码错误"
                    _loginResult.postValue(Result.failure(Exception(message)))
                }

            } catch (e: Exception) {
                _loginResult.postValue(Result.failure(e))
            }
        }
    }

    fun register(phone: String, psw: String) {
        viewModelScope.launch {
            try {
                val resultText = withContext(Dispatchers.IO) {
                    val url = java.net.URL("http://39.102.61.160:5000/api/register")
                    val connection = url.openConnection() as java.net.HttpURLConnection

                    connection.requestMethod = "POST"
                    connection.setRequestProperty("Content-Type", "application/json")
                    connection.doOutput = true
                    connection.connectTimeout = 5000
                    connection.readTimeout = 5000

                    val jsonInput = """
                    {
                        "phone": "$phone",
                        "password": "$psw"
                    }
                """.trimIndent()

                    connection.outputStream.use {
                        it.write(jsonInput.toByteArray())
                        it.flush()
                    }

                    val inputStream = if (connection.responseCode == 200) {
                        connection.inputStream
                    } else {
                        connection.errorStream
                    }

                    val text = inputStream.bufferedReader().readText()
                    connection.disconnect()
                    text
                }

                // 👇 关键：解析返回 JSON
                if (resultText.contains("\"success\": true")) {
                    _registerResult.postValue(Result.success(UserEntity(phone = phone, password = psw)))
                } else {
                    _registerResult.postValue(Result.failure(Exception("注册失败")))
                }

            } catch (e: Exception) {
                _registerResult.postValue(Result.failure(e))
            }
        }
    }
}