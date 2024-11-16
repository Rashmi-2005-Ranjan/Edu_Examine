package com.example.eduexamine.StudentActivityFragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eduexamine.R
import java.text.SimpleDateFormat
import java.util.*

class ExamHistoryAdapter(private val examResults: List<ExamHistoryFragment.ExamResult>) :
    RecyclerView.Adapter<ExamHistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_exam_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = examResults[position]
        holder.examIdTextView.text = "Exam ID: ${result.examId}"
        holder.scoreTextView.text = "Score: ${result.scoredMarks} / ${result.totalMarks}"
        holder.timestampTextView.text = "Date: ${formatDate(result.timestamp)}"
    }

    override fun getItemCount(): Int = examResults.size

    private fun formatDate(date: Date?): String {
        return if (date != null) {
            SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(date)
        } else {
            "N/A"
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val examIdTextView: TextView = view.findViewById(R.id.examIdTextView)
        val scoreTextView: TextView = view.findViewById(R.id.scoreTextView)
        val timestampTextView: TextView = view.findViewById(R.id.timestampTextView)
    }
}
