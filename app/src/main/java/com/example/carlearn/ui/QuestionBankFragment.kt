package com.example.carlearn.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.carlearn.core.ui.base.BaseFragment
import com.example.carlearn.databinding.FragmentQuestionBankBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carlearn.core.database.network.RetrofitClient
import com.example.carlearn.core.database.network.QuestionsResponse
import retrofit2.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.lifecycle.lifecycleScope

@AndroidEntryPoint
class QuestionBankFragment : BaseFragment<FragmentQuestionBankBinding>() {

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentQuestionBankBinding {
        return FragmentQuestionBankBinding.inflate(inflater, container, false)
    }

    override fun initView() {

        binding.recyclerViewQuestions.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            try {
                val response: Response<QuestionsResponse> =
                    withContext(Dispatchers.IO) {
                        RetrofitClient.questionApi.getQuestions()
                    }

                if (response.isSuccessful && response.body()?.success == true) {
                    val list = response.body()?.data ?: emptyList()

                    binding.recyclerViewQuestions.adapter = QuestionBankAdapter(list)
                } else {
                    android.widget.Toast.makeText(
                        requireContext(),
                        "加载失败",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                }

            } catch (e: Exception) {
                android.widget.Toast.makeText(
                    requireContext(),
                    "错误：${e.message}",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}