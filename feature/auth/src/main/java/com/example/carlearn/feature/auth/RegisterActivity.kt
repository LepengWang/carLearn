package com.example.carlearn.feature.auth

import android.widget.Toast
import androidx.activity.viewModels
import com.example.carlearn.core.ui.base.BaseActivity
import com.example.carlearn.feature.auth.databinding.ActivityRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : BaseActivity<ActivityRegisterBinding>() {

    private val viewModel: AuthViewModel by viewModels()

    override fun inflateBinding(): ActivityRegisterBinding {
        return ActivityRegisterBinding.inflate(layoutInflater)
    }

    override fun initView() {
        // 观察注册结果
        viewModel.registerResult.observe(this) { result ->
            result.onSuccess {
                Toast.makeText(this, "注册成功，请登录", Toast.LENGTH_SHORT).show()
                finish()
            }.onFailure {
                Toast.makeText(this, it.message ?: "注册失败", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnRegister.setOnClickListener {
            val phone = binding.etPhone.text.toString()
            val psw = binding.etPassword.text.toString()
            val confirmPsw = binding.etConfirmPassword.text.toString()

            if (phone.isEmpty() || psw.isEmpty()) {
                Toast.makeText(this, "请填写完整信息", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (psw != confirmPsw) {
                Toast.makeText(this, "两次密码输入不一致", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.register(phone, psw)
        }

        binding.tvBackLogin.setOnClickListener {
            finish()
        }
    }
}