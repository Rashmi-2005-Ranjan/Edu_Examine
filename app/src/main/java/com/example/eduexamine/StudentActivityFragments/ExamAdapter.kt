// ExamAdapter.kt
package com.example.eduexamine

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExamAdapter(
    private val exams: List<Exam>,
    private val onAttemptClick: (Exam) -> Unit
) : RecyclerView.Adapter<ExamAdapter.ExamViewHolder>() {

    inner class ExamViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvExamTitle: TextView = view.findViewById(R.id.tvExamTitle)
        val btnAttempt: Button = view.findViewById(R.id.btnAttempt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExamViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.exam_item, parent, false)
        return ExamViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExamViewHolder, position: Int) {
        val exam = exams[position]
        holder.tvExamTitle.text = exam.title
        holder.btnAttempt.setOnClickListener {
            onAttemptClick(exam)
        }
    }

    override fun getItemCount(): Int = exams.size
}
