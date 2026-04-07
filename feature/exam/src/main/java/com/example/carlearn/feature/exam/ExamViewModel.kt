package com.example.carlearn.feature.exam

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.carlearn.core.database.entity.QuestionEntity
import com.example.carlearn.core.database.repository.QuestionRepository
import com.example.carlearn.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExamViewModel @Inject constructor(
    private val repository: QuestionRepository
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
            // 1. 开启本地数据库监听 (响应式 UI)
            repository.getQuestionsByCategory(category).collectLatest { list ->
                _questions.value = list
            }
        }
        
        // 2. 触发网络同步
        viewModelScope.launch {
            repository.syncQuestions()
        }
    }

    fun selectOption(option: String) {
        if (_selectedOption.value != null) return

        val currentQ = _questions.value?.getOrNull(_currentQuestionIndex.value ?: 0) ?: return
        _selectedOption.value = option
        val correct = option == currentQ.answer
        _isCorrect.value = correct

        if (!correct) {
            viewModelScope.launch {
                repository.updateQuestion(currentQ.copy(wrongCount = currentQ.wrongCount + 1))
            }
        }
    }

    fun nextQuestion() {
        val nextIndex = (_currentQuestionIndex.value ?: 0) + 1
        if (nextIndex < (_questions.value?.size ?: 0)) {
            _currentQuestionIndex.value = nextIndex
            resetState()
        }
    }

    fun previousQuestion() {
        val prevIndex = (_currentQuestionIndex.value ?: 0) - 1
        if (prevIndex >= 0) {
            _currentQuestionIndex.value = prevIndex
            resetState()
        }
    }

    private fun resetState() {
        _selectedOption.value = null
        _isCorrect.value = null
    }
}