package com.example.eduexamine

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class MSQ : AppCompatActivity() {

    private lateinit var questionEditText: EditText
    private lateinit var optionAEditText: EditText
    private lateinit var optionBEditText: EditText
    private lateinit var optionCEditText: EditText
    private lateinit var optionDEditText: EditText
    private lateinit var saveQuestionButton: Button
    private lateinit var addImageButton: Button
    private lateinit var questionListLayout: LinearLayout
    private lateinit var imageView: ImageView
    private lateinit var scrollView: ScrollView

    // Initialize Firestore
    private val db = FirebaseFirestore.getInstance()
    private var examId: String? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_msq)

        // Retrieve examId from intent
        examId = intent.getStringExtra("examId")

        if (examId.isNullOrEmpty()) {
            Toast.makeText(this, "Exam ID is missing", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Initialize UI elements
        questionEditText = findViewById(R.id.msqQuestion)
        optionAEditText = findViewById(R.id.op1msq)
        optionBEditText = findViewById(R.id.op2msq)
        optionCEditText = findViewById(R.id.op3msq)
        optionDEditText = findViewById(R.id.op4msq)
        saveQuestionButton = findViewById(R.id.button_add_question)
        addImageButton = findViewById(R.id.button_add_image)
        questionListLayout = findViewById(R.id.question_list)
        imageView = findViewById(R.id.imageView)
        scrollView = findViewById(R.id.scrollView)

        // Set up the button click listener to save the question
        saveQuestionButton.setOnClickListener {
            saveQuestion()
        }

        // Set up the button click listener for adding images
        addImageButton.setOnClickListener {
            Toast.makeText(this, "Image button clicked!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveQuestion() {
        // Gather input data
        val question = questionEditText.text.toString().trim()
        val optionA = optionAEditText.text.toString().trim()
        val optionB = optionBEditText.text.toString().trim()
        val optionC = optionCEditText.text.toString().trim()
        val optionD = optionDEditText.text.toString().trim()

        // Validate inputs
        if (question.isEmpty() || optionA.isEmpty() || optionB.isEmpty() || optionC.isEmpty() || optionD.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Prepare data for Firestore
        val msqData = hashMapOf(
            "question" to question,
            "option1" to optionA,
            "option2" to optionB,
            "option3" to optionC,
            "option4" to optionD,
            "type" to "MSQ",
            "mark" to 1
        )

        // Save data to Firestore in the "exam" collection's "questions" sub-collection
        db.collection("exams").document(examId!!).collection("questions").add(msqData)
            .addOnSuccessListener {
                Toast.makeText(this, "Question saved successfully!", Toast.LENGTH_SHORT).show()
                clearFields()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error saving question: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun clearFields() {
        questionEditText.text?.clear()
        optionAEditText.text?.clear()
        optionBEditText.text?.clear()
        optionCEditText.text?.clear()
        optionDEditText.text?.clear()
    }
}
