// ShowExamAdapter.kt
package com.example.eduexamine.StudentActivityFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.eduexamine.R
import com.example.eduexamine.ShowExam

class ShowExamAdapter(
    private val exams: List<ShowExam>,
    private val onStartClick: (ShowExam) -> Unit
) : RecyclerView.Adapter<ShowExamAdapter.ViewHolderClass>() {

    inner class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title_txrv3)
        val date: TextView = itemView.findViewById(R.id.desc_txtv4)
        val examId: TextView = itemView.findViewById(R.id.desc_txtv3)
        val btnStart: Button = itemView.findViewById(R.id.attemptButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_show_exam, parent, false)
        return ViewHolderClass(view)
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val exam = exams[position]
        holder.title.text = exam.title
        holder.date.text = exam.date
        holder.examId.text = exam.examId
        holder.btnStart.setOnClickListener {
            // Invoke the click listener with the current exam
            onStartClick(exam)
        }
    }

    override fun getItemCount(): Int = exams.size
}