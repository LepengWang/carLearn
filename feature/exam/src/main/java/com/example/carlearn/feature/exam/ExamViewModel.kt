package com.example.carlearn.feature.exam

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.carlearn.core.database.dao.QuestionDao
import com.example.carlearn.core.database.entity.QuestionEntity
import com.example.carlearn.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExamViewModel @Inject constructor(
    private val questionDao: QuestionDao
) : BaseViewModel() {

    private val _questions = MutableLiveData<List<QuestionEntity>>()
    val questions: LiveData<List<QuestionEntity>> = _questions

    private val _currentQuestionIndex = MutableLiveData(0)
    val currentQuestionIndex: LiveData<Int> = _currentQuestionIndex

    private val _selectedOption = MutableLiveData<String?>(null)
    val selectedOption: LiveData<String?> = _selectedOption

    private val _isCorrect = MutableLiveData<Boolean?>(null)
    val isCorrect: LiveData<Boolean?> = _isCorrect

    fun loadQuestions(category: String) {
        viewModelScope.launch {
            // 这里我们先获取一次全量题目，后续可以优化为分页
            val list = questionDao.getQuestionsByCategory(category).first()
            if (list.isEmpty()) {
                // 如果数据库为空，先插入一些模拟数据方便演示
                mockInitialData(category)
                _questions.value = questionDao.getQuestionsByCategory(category).first()
            } else {
                _questions.value = list
            }
        }
    }

    private suspend fun mockInitialData(category: String) {
        val mockList = listOf(
            QuestionEntity(
                id = 1,
                question = "驾驶机动车在道路上违反道路交通安全法的行为，属于什么行为？",
                optionA = "违章行为",
                optionB = "违法行为",
                optionC = "过失行为",
                optionD = "违规行为",
                answer = "B",
                explanation = "《道路交通安全法》明确规定，违反道路交通安全法律、法规的行为属于违法行为。",
                category = category,
                type = "单选题"
            ),
            QuestionEntity(
                id = 2,
                question = "机动车驾驶证有效期分为多少年？",
                optionA = "4年、8年、20年",
                optionB = "5年、10年、20年",
                optionC = "6年、10年、长期",
                optionD = "6年、12年、长期",
                answer = "C",
                explanation = "机动车驾驶证有效期分为六年、十年和长期。",
                category = category,
                type = "单选题"
            )
        )
        questionDao.insertQuestions(mockList)
    }

    fun selectOption(option: String) {
        if (_selectedOption.value != null) return // 已答题不能重复点

        val currentQ = _questions.value?.getOrNull(_currentQuestionIndex.value ?: 0) ?: return
        _selectedOption.value = option
        val correct = option == currentQ.answer
        _isCorrect.value = correct

        if (!correct) {
            // 如果错了，增加错题计数
            viewModelScope.launch {
                questionDao.updateQuestion(currentQ.copy(wrongCount = currentQ.wrongCount + 1))
            }
        }
    }

    fun nextQuestion() {
        val nextIndex = (_currentQuestionIndex.value ?: 0) + 1
        if (nextIndex < (_questions.value?.size ?: 0)) {
            _currentQuestionIndex.value = nextIndex
            _selectedOption.value = null
            _isCorrect.value = null
        }
    }

    fun previousQuestion() {
        val prevIndex = (_currentQuestionIndex.value ?: 0) - 1
        if (prevIndex >= 0) {
            _currentQuestionIndex.value = prevIndex
            _selectedOption.value = null
            _isCorrect.value = null
        }
    }
}