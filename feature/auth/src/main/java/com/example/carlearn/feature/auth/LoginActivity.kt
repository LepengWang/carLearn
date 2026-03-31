package com.example.carlearn.feature.auth

import android.content.Intent
import android.widget.Toast
import androidx.activity.viewModels
import com.example.carlearn.core.ui.base.BaseActivity
import com.example.carlearn.feature.auth.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private val viewModel: AuthViewModel by viewModels()

    override fun inflateBinding(): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun initView() {
        // 观察登录结果
        viewModel.loginResult.observe(this) { result ->
            result.onSuccess {
                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show()
                val intent = Intent()
                intent.setClassName(this, "com.example.carlearn.MainActivity")
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }.onFailure {
                Toast.makeText(this, it.message ?: "登录失败", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnLogin.setOnClickListener {
            val phone = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            if (phone.isNotEmpty() && password.isNotEmpty()) {
                viewModel.login(phone, password)
            } else {
                Toast.makeText(this, "请输入账号和密码", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}