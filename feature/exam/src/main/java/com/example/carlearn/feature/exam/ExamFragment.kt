package com.example.carlearn.feature.exam

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.carlearn.core.ui.base.BaseFragment
import com.example.carlearn.feature.exam.databinding.FragmentExamBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.lifecycleScope

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.widget.Toast
import retrofit2.Response
import com.example.carlearn.core.database.network.QuestionsResponse
import com.example.carlearn.core.database.network.RetrofitClient
import com.example.carlearn.core.database.network.QuestionItem


@AndroidEntryPoint
class ExamFragment : BaseFragment<FragmentExamBinding>() {

    private val viewModel: ExamViewModel by viewModels()
    private var backendQuestions: List<QuestionItem> = emptyList()
    private var backendIndex: Int = 0

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentExamBinding {
        return FragmentExamBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        // 绑定选项点击事件
        binding.btnOptionA.setOnClickListener { viewModel.selectOption("A") }
        binding.btnOptionB.setOnClickListener { viewModel.selectOption("B") }
        binding.btnOptionC.setOnClickListener { viewModel.selectOption("C") }
        binding.btnOptionD.setOnClickListener { viewModel.selectOption("D") }

        // 翻页控制
        //binding.btnNext.setOnClickListener { viewModel.nextQuestion() }
        //binding.btnPrev.setOnClickListener { viewModel.previousQuestion() }

        binding.btnNext.setOnClickListener {
            if (backendQuestions.isNotEmpty() && backendIndex < backendQuestions.lastIndex) {
                backendIndex++
                showBackendQuestion(backendIndex)
            } else {
                Toast.makeText(requireContext(), "已经是最后一题", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnPrev.setOnClickListener {
            if (backendQuestions.isNotEmpty() && backendIndex > 0) {
                backendIndex--
                showBackendQuestion(backendIndex)
            } else {
                Toast.makeText(requireContext(), "已经是第一题", Toast.LENGTH_SHORT).show()
            }
        }

        // 观察数据变化
        observeViewModel()

        // 初始加载题目 (默认加载科目一)
        //viewModel.loadQuestions("科目一")

        lifecycleScope.launch {
            try {
                val response: Response<QuestionsResponse> =
                    withContext(Dispatchers.IO) {
                        RetrofitClient.questionApi.getQuestions()
                    }

                if (response.isSuccessful && response.body()?.success == true) {

                    backendQuestions = response.body()?.data ?: emptyList()
                    backendIndex = 0

                    if (backendQuestions.isNotEmpty()) {
                        showBackendQuestion(backendIndex)
                    } else {
                        Toast.makeText(requireContext(), "后端没有题目", Toast.LENGTH_LONG).show()
                    }

                } else {
                    Toast.makeText(requireContext(), "获取题库失败", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    requireContext(),
                    "错误类型：${e.javaClass.simpleName}\n内容：${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    private fun observeViewModel() {
        // 1. 观察题目列表和索引
        viewModel.questions.observe(viewLifecycleOwner) { list ->
            updateUI(list, viewModel.currentQuestionIndex.value ?: 0)
        }

        viewModel.currentQuestionIndex.observe(viewLifecycleOwner) { index ->
            updateUI(viewModel.questions.value, index)
        }

        // 2. 观察答题状态：变色反馈
        viewModel.selectedOption.observe(viewLifecycleOwner) { selected ->
            if (selected == null) {
                resetOptionStyles()
                binding.cardExplanation.visibility = View.GONE
            } else {
                handleOptionFeedback(selected)
            }
        }
    }

    private fun updateUI(questions: List<com.example.carlearn.core.database.entity.QuestionEntity>?, index: Int) {
        val question = questions?.getOrNull(index) ?: return
        
        binding.tvProgress.text = "第 ${index + 1} / ${questions.size} 题"
        binding.tvQuestionType.text = question.type
        binding.tvQuestion.text = question.question
        binding.btnOptionA.text = "A. ${question.optionA}"
        binding.btnOptionB.text = "B. ${question.optionB}"
        binding.btnOptionC.text = "C. ${question.optionC}"
        binding.btnOptionD.text = "D. ${question.optionD}"
        binding.tvExplanation.text = question.explanation
    }

    private fun handleOptionFeedback(selected: String) {
        val questions = viewModel.questions.value ?: return
        val currentQ = questions.getOrNull(viewModel.currentQuestionIndex.value ?: 0) ?: return
        val correctOption = currentQ.answer

        // 无论对错，显示解析卡片
        binding.cardExplanation.visibility = View.VISIBLE

        // 核心变色逻辑
        val optionsMap = mapOf(
            "A" to binding.btnOptionA,
            "B" to binding.btnOptionB,
            "C" to binding.btnOptionC,
            "D" to binding.btnOptionD
        )

        // 1. 将正确答案标绿
        optionsMap[correctOption]?.setBackgroundResource(com.example.carlearn.core.ui.R.drawable.bg_option_correct)

        // 2. 如果选错了，将选中的标红
        if (selected != correctOption) {
            optionsMap[selected]?.setBackgroundResource(com.example.carlearn.core.ui.R.drawable.bg_option_error)
        }
    }

    private fun resetOptionStyles() {
        val defaultBg = com.example.carlearn.core.ui.R.drawable.bg_option_default
        binding.btnOptionA.setBackgroundResource(defaultBg)
        binding.btnOptionB.setBackgroundResource(defaultBg)
        binding.btnOptionC.setBackgroundResource(defaultBg)
        binding.btnOptionD.setBackgroundResource(defaultBg)
    }

    private fun showBackendQuestion(index: Int) {
        val q = backendQuestions.getOrNull(index) ?: return

        binding.tvProgress.text = "第 ${index + 1} / ${backendQuestions.size} 题"
        binding.tvQuestionType.text = "后端题目"
        binding.tvQuestion.text = q.question

        binding.btnOptionA.text = "A. " + q.options.getOrNull(0).orEmpty()
        binding.btnOptionB.text = "B. " + q.options.getOrNull(1).orEmpty()
        binding.btnOptionC.text = "C. " + q.options.getOrNull(2).orEmpty()
        binding.btnOptionD.text = "D. " + q.options.getOrNull(3).orEmpty()

        binding.tvExplanation.text = "正确答案：${q.answer}"
        binding.cardExplanation.visibility = View.VISIBLE
    }
}