package com.example.eduexamine.StudentActivityFragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eduexamine.R




class ExamHistoryAdapter(private val examList: List<ExamHistory>) : RecyclerView.Adapter<ExamHistoryAdapter.ExamViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExamViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_exam_history, parent, false)
        return ExamViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExamViewHolder, position: Int) {
        val exam = examList[position]
        holder.bind(exam)
    }

    override fun getItemCount() = examList.size

    class ExamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvExamTitle: TextView = itemView.findViewById(R.id.tvExamTitle)
        private val tvExamDate: TextView = itemView.findViewById(R.id.tvExamDate)
        private val tvScore: TextView = itemView.findViewById(R.id.tvScore)

        fun bind(exam: ExamHistory) {
            tvExamTitle.text = exam.title
            tvExamDate.text = "Date: ${exam.date}"
            tvScore.text = "Score: ${exam.score}"
        }
    }
}
