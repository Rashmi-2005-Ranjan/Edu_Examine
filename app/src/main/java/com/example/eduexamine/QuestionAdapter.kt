package com.example.eduexamine

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QuestionAdapter(
    private val questionList: List<QuestionDataClass>,
    private val onSetAnswerClick: (QuestionDataClass, String) -> Unit
) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val questionTextView: TextView = itemView.findViewById(R.id.title_txrv3)
        val answerEditText: EditText = itemView.findViewById(R.id.desc_txtv3)
        val setAnswerButton: Button = itemView.findViewById(R.id.attemptButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_question, parent, false)
        return QuestionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val currentQuestion = questionList[position]
        holder.questionTextView.text = currentQuestion.questionText
        holder.setAnswerButton.setOnClickListener {
            val answerText = holder.answerEditText.text.toString()
            onSetAnswerClick(currentQuestion, answerText)
        }
    }

    override fun getItemCount(): Int = questionList.size
}
