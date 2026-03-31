package com.example.carlearn

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.carlearn.core.database.UserPreferences
import com.example.carlearn.databinding.ActivityMainBinding
import com.example.carlearn.feature.auth.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import android.widget.Toast
import androidx.lifecycle.lifecycleScope

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


import com.example.carlearn.core.database.network.LoginRequest
import com.example.carlearn.core.database.network.RetrofitClient
import com.example.carlearn.core.database.network.QuestionsResponse
import com.example.carlearn.core.database.network.AuthResponse
import retrofit2.Response

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    
    @Inject
    lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 逻辑判断：从 UserPreferences 检查真实登录状态
        if (!userPreferences.isLoggedIn()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 初始化 Navigation
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // 绑定底部导航栏
        binding.bottomNav.setupWithNavController(navController)

        lifecycleScope.launch {
            lifecycleScope.launch {
                try {
                    val response: Response<QuestionsResponse> =
                        withContext(Dispatchers.IO) {
                            RetrofitClient.questionApi.getQuestions()
                        }

                    if (response.isSuccessful && response.body()?.success == true) {
                        val questions = response.body()?.data ?: emptyList()
                        Toast.makeText(
                            this@MainActivity,
                            "题目数量：${questions.size}",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "获取题库失败",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        this@MainActivity,
                        "错误：${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        }

    } }