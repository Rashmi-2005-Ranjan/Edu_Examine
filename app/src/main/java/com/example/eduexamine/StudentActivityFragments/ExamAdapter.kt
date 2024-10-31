package com.example.eduexamine.StudentActivityFragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eduexamine.R

class ExamAdapter(private val exams: List<String>, private val onAttemptClick: (String) -> Unit) :
    RecyclerView.Adapter<ExamAdapter.ExamViewHolder>() {

    inner class ExamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val examTitle: TextView = itemView.findViewById(R.id.examTitle)
        val buttonAttempt: Button = itemView.findViewById(R.id.buttonAttempt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExamViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.exam_item, parent, false)
        return ExamViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExamViewHolder, position: Int) {
        val exam = exams[position]
        holder.examTitle.text = exam

        holder.buttonAttempt.setOnClickListener {
            onAttemptClick(exam)
        }
    }

    override fun getItemCount() = exams.size
}
