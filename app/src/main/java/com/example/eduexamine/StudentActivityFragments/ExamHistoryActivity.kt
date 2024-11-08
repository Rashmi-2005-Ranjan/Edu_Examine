package com.example.eduexamine.StudentActivityFragments

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eduexamine.R

class ExamHistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var examHistoryAdapter: ExamHistoryAdapter
    private lateinit var examList: MutableList<ExamHistory>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exam_history)

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewExamHistory)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize mock data
        examList = getMockExamHistory()

        // Initialize Adapter
        examHistoryAdapter = ExamHistoryAdapter(examList)
        recyclerView.adapter = examHistoryAdapter
    }

    private fun getMockExamHistory(): MutableList<ExamHistory> {
        return mutableListOf(
            ExamHistory("Math Exam", "01/01/2023", "85/100"),
            ExamHistory("Science Exam", "01/15/2023", "90/100"),
            ExamHistory("History Exam", "02/10/2023", "75/100"),
            ExamHistory("Geography Exam", "02/20/2023", "88/100"),
            ExamHistory("Physics Exam", "03/05/2023", "92/100")
        )
    }
}
