package com.example.eduexamine.StudentActivityFragments

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eduexamine.R

class Available_exam : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_available_exam)

        val examListView = findViewById<ListView>(R.id.examListView)

        val exams = listOf(
            "Mathematics Exam",
            "Physics Exam",
            "Chemistry Exam",
            "Biology Exam",
            "History Exam"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, exams)
        examListView.adapter = adapter

        examListView.setOnItemClickListener { _, _, position, _ ->
            val selectedExam = exams[position]

        }
    }
}