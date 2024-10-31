package com.example.eduexamine.StudentActivityFragments

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eduexamine.R

class AvailableExam : AppCompatActivity() {

    private lateinit var recyclerViewExams: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_available_exam)

        recyclerViewExams = findViewById(R.id.recyclerViewExams)

        val examList = listOf("Math Exam", "Science Exam", "History Exam")

        recyclerViewExams.adapter = ExamAdapter(examList) { exam ->
            val intent = Intent(this, AttemptExam::class.java)
            intent.putExtra("EXAM_NAME", exam)
            startActivity(intent)
        }

        recyclerViewExams.layoutManager = LinearLayoutManager(this)
    }
}
