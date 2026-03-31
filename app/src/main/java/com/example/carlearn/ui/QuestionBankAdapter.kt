package com.example.carlearn.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.carlearn.core.database.network.QuestionItem
import com.example.carlearn.databinding.ItemQuestionBinding

class QuestionBankAdapter(
    private val questions: List<QuestionItem>
) : RecyclerView.Adapter<QuestionBankAdapter.QuestionViewHolder>() {

    inner class QuestionViewHolder(val binding: ItemQuestionBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val binding = ItemQuestionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return QuestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questions[position]
        holder.binding.tvQuestionTitle.text = "${position + 1}. ${question.question}"
        holder.binding.tvQuestionAnswer.text = "答案：${question.answer}"
    }

    override fun getItemCount(): Int = questions.size
}