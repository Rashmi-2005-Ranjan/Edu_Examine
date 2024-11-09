package com.example.eduexamine

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class SetQuestionAnswerActivity : AppCompatActivity() {
    private lateinit var questionsRecyclerView: RecyclerView
    private lateinit var questionAdapter: QuestionAdapter
    private lateinit var questionList: ArrayList<QuestionDataClass>
    private val db = FirebaseFirestore.getInstance()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_question_answer)

        // Get the exam ID from the Intent
        val examId = intent.getStringExtra("EXAM_ID")

        // Initialize RecyclerView
        questionsRecyclerView = findViewById(R.id.rectangles_recycler_view)
        questionsRecyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize the question list
        questionList = arrayListOf()

        // Fetch questions related to the exam
        examId?.let {
            fetchQuestionsForExam(examId)
        }
    }

    // Fetch the question documents for the specific exam
    private fun fetchQuestionsForExam(examId: String) {
        db.collection("exams")
            .document(examId)  // Use the examId passed via Intent
            .collection("questions")  // Get the "questions" sub-collection
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Toast.makeText(this, "No questions found for this exam.", Toast.LENGTH_SHORT).show()
                } else {
                    questionList.clear()
                    for (document in documents) {
                        val questionText = document.getString("question") ?: "No Question Text"
                        val questionId = document.id
                        questionList.add(QuestionDataClass(questionId, questionText))
                    }

                    // Now set up the RecyclerView with the questions
                    setupRecyclerView()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load questions.", Toast.LENGTH_SHORT).show()
            }
    }

    // Setup the RecyclerView with the fetched questions
    private fun setupRecyclerView() {
        questionAdapter = QuestionAdapter(questionList) { selectedQuestion, answerText ->
            // Handle setting the answer for the selected question
            handleSetAnswer(selectedQuestion, answerText)
        }
        questionsRecyclerView.adapter = questionAdapter
        questionAdapter.notifyDataSetChanged()
    }

    // Handle saving the answer for a question
    private fun handleSetAnswer(question: QuestionDataClass, answerText: String) {
        if (answerText.isBlank()) {
            Toast.makeText(this, "Please enter an answer", Toast.LENGTH_SHORT).show()
            return
        }

        // Save the answer in Firestore under the "answers" collection
        db.collection("actual_answers_by_admin")
            .document(question.questionId)  // Use questionId to store the answer
            .set(mapOf("answerText" to answerText, "examId" to intent.getStringExtra("EXAM_ID")))
            .addOnSuccessListener {
                Toast.makeText(this, "Answer saved successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to save answer", Toast.LENGTH_SHORT).show()
            }
    }
}
