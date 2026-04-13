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

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun loadQuestions(category: String) {
        _isLoading.value = true
        viewModelScope.launch {
            repository.getQuestionsByCategory(category).collectLatest { list ->
                _questions.value = list
                if (list.isNotEmpty()) {
                    _isLoading.value = false
                }
            }
        }

        viewModelScope.launch {
            try {
                repository.syncQuestions()
            } catch (e: Exception) {
                if (e !is kotlinx.coroutines.CancellationException) {
                    _errorMessage.value = "同步题库失败: ${e.message}"
                }
            } finally {
                // 如果本地有数据，就不显示加载中了
                if (_questions.value?.isNotEmpty() == true) {
                    _isLoading.value = false
                }
            }
        }
    }

    fun selectOption(option: String) {
        // 如果已经答过题，或者没有题目，则不处理
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
            resetAnswerState()
        }
    }

    fun previousQuestion() {
        val prevIndex = (_currentQuestionIndex.value ?: 0) - 1
        if (prevIndex >= 0) {
            _currentQuestionIndex.value = prevIndex
            resetAnswerState()
        }
    }

    private fun resetAnswerState() {
        _selectedOption.value = null
        _isCorrect.value = null
    }
}