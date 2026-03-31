package com.example.carlearn.feature.exam

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.carlearn.core.database.network.QuestionItem
import com.example.carlearn.core.database.network.QuestionsResponse
import com.example.carlearn.core.database.network.RetrofitClient
import com.example.carlearn.core.ui.base.BaseFragment
import com.example.carlearn.feature.exam.databinding.FragmentExamBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

@AndroidEntryPoint
class ExamFragment : BaseFragment<FragmentExamBinding>() {

    private var backendQuestions: List<QuestionItem> = emptyList()
    private var backendIndex: Int = 0
    private var hasAnsweredCurrentQuestion: Boolean = false

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentExamBinding {
        return FragmentExamBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        // 选项点击：做题逻辑
        binding.btnOptionA.setOnClickListener { checkBackendAnswer("A") }
        binding.btnOptionB.setOnClickListener { checkBackendAnswer("B") }
        binding.btnOptionC.setOnClickListener { checkBackendAnswer("C") }
        binding.btnOptionD.setOnClickListener { checkBackendAnswer("D") }

        // 翻页控制
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

        // 加载后端题目
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

    private fun showBackendQuestion(index: Int) {
        val q = backendQuestions.getOrNull(index) ?: return

        hasAnsweredCurrentQuestion = false
        resetOptionStyles()

        binding.tvProgress.text = "第 ${index + 1} / ${backendQuestions.size} 题"
        binding.tvQuestionType.text = "顺序练习"
        binding.tvQuestion.text = q.question

        val optionA = q.options.getOrNull(0).orEmpty()
        val optionB = q.options.getOrNull(1).orEmpty()
        val optionC = q.options.getOrNull(2).orEmpty()
        val optionD = q.options.getOrNull(3).orEmpty()

        binding.btnOptionA.text = "A. $optionA"
        binding.btnOptionB.text = "B. $optionB"
        binding.btnOptionC.text = "C. $optionC"
        binding.btnOptionD.text = "D. $optionD"

        binding.btnOptionA.visibility = if (optionA.isNotEmpty()) View.VISIBLE else View.GONE
        binding.btnOptionB.visibility = if (optionB.isNotEmpty()) View.VISIBLE else View.GONE
        binding.btnOptionC.visibility = if (optionC.isNotEmpty()) View.VISIBLE else View.GONE
        binding.btnOptionD.visibility = if (optionD.isNotEmpty()) View.VISIBLE else View.GONE

        // 进入题目时先隐藏答案解析
        binding.tvExplanation.text = ""
        binding.cardExplanation.visibility = View.GONE
    }

    private fun checkBackendAnswer(userAnswer: String) {
        if (hasAnsweredCurrentQuestion) {
            Toast.makeText(requireContext(), "这题已经作答，请切换下一题", Toast.LENGTH_SHORT).show()
            return
        }

        val q = backendQuestions.getOrNull(backendIndex) ?: return
        val correctAnswer = q.answer

        val selectedText = when (userAnswer) {
            "A" -> q.options.getOrNull(0).orEmpty()
            "B" -> q.options.getOrNull(1).orEmpty()
            "C" -> q.options.getOrNull(2).orEmpty()
            "D" -> q.options.getOrNull(3).orEmpty()
            else -> ""
        }

        hasAnsweredCurrentQuestion = true

        if (selectedText == correctAnswer) {
            Toast.makeText(requireContext(), "回答正确", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(
                requireContext(),
                "回答错误，正确答案：$correctAnswer",
                Toast.LENGTH_SHORT
            ).show()
        }

        highlightAnswer(userAnswer, q)
        binding.tvExplanation.text = "正确答案：$correctAnswer"
        binding.cardExplanation.visibility = View.VISIBLE
    }

    private fun highlightAnswer(userAnswer: String, q: QuestionItem) {
        val optionMap = mapOf(
            "A" to binding.btnOptionA,
            "B" to binding.btnOptionB,
            "C" to binding.btnOptionC,
            "D" to binding.btnOptionD
        )

        val correctLetter = when (q.answer) {
            q.options.getOrNull(0).orEmpty() -> "A"
            q.options.getOrNull(1).orEmpty() -> "B"
            q.options.getOrNull(2).orEmpty() -> "C"
            q.options.getOrNull(3).orEmpty() -> "D"
            else -> ""
        }

        // 正确答案标绿
        optionMap[correctLetter]?.setBackgroundResource(
            com.example.carlearn.core.ui.R.drawable.bg_option_correct
        )

        // 如果选错了，用户选项标红
        if (userAnswer != correctLetter) {
            optionMap[userAnswer]?.setBackgroundResource(
                com.example.carlearn.core.ui.R.drawable.bg_option_error
            )
        }
    }

    private fun resetOptionStyles() {
        val defaultBg = com.example.carlearn.core.ui.R.drawable.bg_option_default
        binding.btnOptionA.setBackgroundResource(defaultBg)
        binding.btnOptionB.setBackgroundResource(defaultBg)
        binding.btnOptionC.setBackgroundResource(defaultBg)
        binding.btnOptionD.setBackgroundResource(defaultBg)
    }
}