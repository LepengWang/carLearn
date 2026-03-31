package com.example.carlearn.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.carlearn.MainActivity
import com.example.carlearn.core.ui.base.BaseFragment
import com.example.carlearn.databinding.FragmentProfileBinding
import com.example.carlearn.feature.auth.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        binding.btnLogout.setOnClickListener {
            // 退出登录：跳转回登录页并清空任务栈
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    override fun initData() {
        // 后续从 Repository 获取真实用户信息
        binding.tvUsername.text = "智驾学员"
    }
}