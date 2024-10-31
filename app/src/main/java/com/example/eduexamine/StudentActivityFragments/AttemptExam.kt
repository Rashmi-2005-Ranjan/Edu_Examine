package com.example.eduexamine.StudentActivityFragments

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eduexamine.R

class AttemptExam : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attempt_exam)

        val examName = intent.getStringExtra("EXAM_NAME")
        // Display or use examName as needed, for example:
        findViewById<TextView>(R.id.textViewExamName).text = examName
    }
}
