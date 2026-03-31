package com.example.carlearn.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.carlearn.R
import com.example.carlearn.core.ui.base.BaseFragment
import com.example.carlearn.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>() {

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentMainBinding {
        return FragmentMainBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        binding.btnOrderPractice.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_examFragment)
        }

        binding.btnMockExam.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_examFragment)
        }
    }
}